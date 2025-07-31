package ru.itis.t_trips.tripinfo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.model.expense.ActualExpenseModel
import ru.itis.t_trips.domain.model.expense.ExpenseModel
import ru.itis.t_trips.domain.model.expense.PlannedExpenseModel
import ru.itis.t_trips.domain.usecase.GetPictureUseCase
import ru.itis.t_trips.domain.usecase.expense.DeletePlannedExpenseUseCase
import ru.itis.t_trips.domain.usecase.expense.GetActualExpensesListUseCase
import ru.itis.t_trips.domain.usecase.expense.GetPlannedExpensesUseCase
import ru.itis.t_trips.domain.usecase.trip.FinishTripUseCase
import ru.itis.t_trips.domain.usecase.trip.GetTripInfoUseCase
import ru.itis.t_trips.domain.usecase.trip.GetTripsListUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.tripinfo.TripInfoScreenEffect
import ru.itis.t_trips.tripinfo.TripInfoScreenEvent
import ru.itis.t_trips.tripinfo.TripInfoScreenState
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
internal class TripInfoViewModel @Inject constructor(
    private val getTripInfoUseCase: GetTripInfoUseCase,
    private val getTripsListUseCase: GetTripsListUseCase,
    private val getPictureUseCase: GetPictureUseCase,
    private val finishTripUseCase: FinishTripUseCase,
    private val getPlannedExpensesUseCase: GetPlannedExpensesUseCase,
    private val getActualExpensesUseCase: GetActualExpensesListUseCase,
    private val deletePlannedExpenseUseCase: DeletePlannedExpenseUseCase,
    private val tripNavigator: TripNavigator,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<TripInfoScreenState>(value = TripInfoScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<TripInfoScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: TripInfoScreenEvent) {
        when (event) {
            is TripInfoScreenEvent.OnInitScreen -> getTripInfo(event.tripId)
            is TripInfoScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is TripInfoScreenEvent.OnEditBtnClick -> tripNavigator.toEditTripScreen(event.tripId)
            is TripInfoScreenEvent.OnLookMembersBtnClick -> navigator.toMembersTripScreen(event.tripId)
            is TripInfoScreenEvent.OnTabSelected -> {
                _pageState.update { currentState ->
                    if (currentState is TripInfoScreenState.TripInfoResult) {
                        currentState.copy(selectedTab = event.tab)
                    } else {
                        currentState
                    }
                }
            }
            is TripInfoScreenEvent.OnTabCategorySelected -> {
                _pageState.update { currentState ->
                    if (currentState is TripInfoScreenState.TripInfoResult) {
                        currentState.copy(selectedCategoryTab = event.tab)
                    } else {
                        currentState
                    }
                }
            }
            is TripInfoScreenEvent.OnActualExpenseItemClick -> tripNavigator.toActualExpenseInfoScreen(expenseId = event.expenseId, tripId = event.tripId)
            is TripInfoScreenEvent.OnAddPlannedExpense -> tripNavigator.toAddPlannedExpense(event.tripId)
            is TripInfoScreenEvent.OnAddActualExpense -> tripNavigator.toAddActualExpense(event.tripId)
            is TripInfoScreenEvent.OnFinishBtnClick -> finishTrip(event.tripId)
            is TripInfoScreenEvent.OnDeletePlannedExpenseIconClick -> deletePlannedExpense(event.expenseId)
        }
    }

    private fun getTripInfo(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                _pageState.value = TripInfoScreenState.Loading
                //delay(2000)
                getTripInfoUseCase(tripId)
            }.onSuccess { tripModel ->
                val tripPicUrl = getPictureUseCase(OtherProperties.FILE_TRIP_PIC_PREFIX, imageId = tripModel.id)
                val isCreator = checkCreator(tripModel.id)
                val plannedExpenses: List<ExpenseModel> = getTripPlannedExpenses(tripModel.id)
                val actualExpenses: List<ExpenseModel> = getTripActualExpenses(tripModel.id)
                _pageState.value = TripInfoScreenState.TripInfoResult(
                    result = tripModel,
                    picUrl = tripPicUrl,
                    isCreator = isCreator,
                    plannedExpenses = plannedExpenses,
                    actualExpenses = actualExpenses,
                )
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    TripInfoScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private suspend fun getTripActualExpenses(tripId: Int): List<ActualExpenseModel> {
        return runCatching {
            getActualExpensesUseCase(tripId)
        }.onFailure {
            _pageEffect.emit(TripInfoScreenEffect.Message(R.string.exception_msg_actual_expenses))
        }.getOrThrow()
    }

    private suspend fun checkCreator(tripId: Int): Boolean {
        return runCatching {
            getTripsListUseCase(onlyCreator = true)
                .any { trip ->
                    trip.id == tripId
                }
        }.onFailure { exception ->
            val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
            _pageEffect.emit(
                TripInfoScreenEffect.Message(
                    message = messageResId
                )
            )
        }.getOrThrow()
    }

    private fun finishTrip(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                finishTripUseCase(tripId)
            }.onSuccess {
                _pageEffect.emit(TripInfoScreenEffect.Message(R.string.msg_finish_trip_success))
                navigator.popBackStack()
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    TripInfoScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private suspend fun getTripPlannedExpenses(tripId: Int): List<PlannedExpenseModel> {
        return runCatching {
            getPlannedExpensesUseCase(tripId)
        }.onFailure {
            _pageEffect.emit(TripInfoScreenEffect.Message(R.string.exception_msg_planned_expenses))
        }.getOrThrow()
    }

    private fun deletePlannedExpense(expenseId: Int) {
        viewModelScope.launch {
            runCatching {
                deletePlannedExpenseUseCase(expenseId)
            }.onSuccess {
                _pageState.update { currentState ->
                    if (currentState is TripInfoScreenState.TripInfoResult) {
                        val currentPlannedExpenses = currentState.plannedExpenses
                        val updatedPlannedExpenses =
                            currentPlannedExpenses.filter { it.id != expenseId }
                        currentState.copy(plannedExpenses = updatedPlannedExpenses)
                    } else {
                        currentState
                    }
                }
                _pageEffect.emit(
                    TripInfoScreenEffect.Message(
                        R.string.msg_delete_data_success
                    )
                )
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    TripInfoScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}