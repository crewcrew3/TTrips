package ru.itis.t_trips.actualexpenseinfo.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.t_trips.actualexpenseinfo.ActualExpenseInfoScreenEffect
import ru.itis.t_trips.actualexpenseinfo.ActualExpenseInfoScreenEvent
import ru.itis.t_trips.actualexpenseinfo.ActualExpenseInfoScreenState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.model.UserProfileModel
import ru.itis.t_trips.domain.usecase.GetPictureUseCase
import ru.itis.t_trips.domain.usecase.SavePictureUseCase
import ru.itis.t_trips.domain.usecase.expense.DeleteActualExpenseUseCase
import ru.itis.t_trips.domain.usecase.expense.GetActualExpenseUseCase
import ru.itis.t_trips.domain.usecase.user.GetUserByIdUseCase
import ru.itis.t_trips.domain.usecase.user.GetUserProfileUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
internal class ActualExpenseInfoViewModel @Inject constructor(
    private val getActualExpenseUseCase: GetActualExpenseUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val deleteActualExpenseUseCase: DeleteActualExpenseUseCase,
    private val getPictureUseCase: GetPictureUseCase,
    private val savePictureUseCase: SavePictureUseCase,
    private val navigator: Navigator,
    private val tripNavigator: TripNavigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<ActualExpenseInfoScreenState>(value = ActualExpenseInfoScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<ActualExpenseInfoScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: ActualExpenseInfoScreenEvent) {
        when (event) {
            is ActualExpenseInfoScreenEvent.OnScreenInit -> loadExpenseInfo(event.expenseId)
            is ActualExpenseInfoScreenEvent.OnBackBtnClick -> tripNavigator.toTripDetailsScreen(event.tripId)
            is ActualExpenseInfoScreenEvent.OnReceiptAdded -> savePicture(
                imageId = event.expenseId,
                sourceUri = event.receipt,
                context = event.context,
            )
            is ActualExpenseInfoScreenEvent.OnDeleteExpenseIconClick ->
                deleteActualExpense(
                    expenseId = event.expenseId,
                    tripId = event.tripId
                )
        }
    }

    private fun loadExpenseInfo(expenseId: Int) {
        viewModelScope.launch {
            runCatching {
                _pageState.value = ActualExpenseInfoScreenState.Loading
                //delay(2000) //чтобы шиммеры красиво поработали
                getActualExpenseUseCase(expenseId)
            }.onSuccess { expense ->
                val expenseReceiptPicUrl = getPictureUseCase(OtherProperties.FILE_EXPENSE_PIC_PREFIX, imageId = expenseId)
                val userPaid = getUserById(expense.paidByUserId)
                val isCreator = expense.paidByUserId == getOurId()
                _pageState.value = ActualExpenseInfoScreenState.Result(
                    expense = expense,
                    picUrl = expenseReceiptPicUrl,
                    participants = expense.participants.map { entry ->
                        val user = getUserById(entry.key)
                        Contact(
                            id = user.id,
                            name = user.firstName + " " + user.lastName,
                            phoneNumber = user.phoneNumber,
                            amount = entry.value
                        )
                    },
                    userPaid = Contact(
                        id = userPaid.id,
                        name = userPaid.firstName + " " + userPaid.lastName,
                        phoneNumber = userPaid.phoneNumber,
                        amount = expense.amount - expense.participants.values.sumOf { it }
                    ),
                    isCreator = isCreator
                )
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    ActualExpenseInfoScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private suspend fun getUserById(userId: Int): UserProfileModel {
        return runCatching {
            getUserByIdUseCase(userId)
        }.onFailure { exception ->
            val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
            _pageEffect.emit(
                ActualExpenseInfoScreenEffect.Message(
                    message = messageResId
                )
            )
        }.getOrThrow()
    }

    private fun savePicture(context: Context?, sourceUri: Uri?, imageId: Int) {
        viewModelScope.launch {
            if (sourceUri != null && context != null) {
                runCatching {
                    savePictureUseCase(
                        keyPrefix = OtherProperties.FILE_EXPENSE_PIC_PREFIX,
                        context = context,
                        sourceUri = sourceUri,
                        imageId = imageId
                    )
                }.onFailure {
                    _pageEffect.emit(
                        ActualExpenseInfoScreenEffect.Message(
                            R.string.exception_msg_picture
                        )
                    )
                }
            }
        }
    }

    private suspend fun getOurId(): Int {
        return runCatching {
            getUserProfileUseCase().id
        }.onFailure { exception ->
            val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
            _pageEffect.emit(
                ActualExpenseInfoScreenEffect.Message(
                    message = messageResId
                )
            )
        }.getOrThrow()
    }

    private fun deleteActualExpense(expenseId: Int, tripId: Int) {
        viewModelScope.launch {
            runCatching {
                deleteActualExpenseUseCase(expenseId)
            }.onSuccess {
                _pageEffect.emit(
                    ActualExpenseInfoScreenEffect.Message(
                        message = R.string.msg_delete_data_success
                    )
                )
                tripNavigator.toTripDetailsScreen(tripId)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    ActualExpenseInfoScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}