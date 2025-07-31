package ru.itis.t_trips.addplannedexpense.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.t_trips.addplannedexpense.AddPlannedExpenseScreenEffect
import ru.itis.t_trips.addplannedexpense.AddPlannedExpenseScreenEvent
import ru.itis.t_trips.addplannedexpense.AddPlannedExpenseScreenState
import ru.itis.t_trips.addplannedexpense.PlannedExpenseFormState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.expense.SavePlannedExpenseUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.utils.ValidatorHelper
import javax.inject.Inject

@HiltViewModel
internal class AddPlannedExpenseViewModel @Inject constructor(
    private val savePlannedExpenseUseCase: SavePlannedExpenseUseCase,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<AddPlannedExpenseScreenState>(value = AddPlannedExpenseScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = PlannedExpenseFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<AddPlannedExpenseScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: AddPlannedExpenseScreenEvent) {
        when (event) {
            is AddPlannedExpenseScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is AddPlannedExpenseScreenEvent.OnSaveBtnClick -> savePlannedExpense(
                tripId = event.tripId,
                title = event.title,
                amount = event.amount,
                category = event.category,
            )
            is AddPlannedExpenseScreenEvent.OnFormFieldChanged ->
                _formState.update { state ->
                    state.copy(
                        title = event.title ?: state.title,
                        amount = event.amount ?: state.amount,
                        category = event.category ?: state.category,
                    )
                }
        }
    }

   private fun savePlannedExpense(
       tripId: Int,
       title: String,
       amount: String,
       category: String,
   ) {
       viewModelScope.launch {
           if (validateExpenseForm(title = title)) {
               runCatching {
                   savePlannedExpenseUseCase(
                       tripId = tripId,
                       title = title,
                       amount = amount,
                       category = category,
                   )
               }.onSuccess {
                   _pageEffect.emit(
                       AddPlannedExpenseScreenEffect.Message(
                           message = R.string.msg_save_data_success
                       )
                   )
                   navigator.popBackStack()
               }.onFailure { exception ->
                   val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                   _pageEffect.emit(
                       AddPlannedExpenseScreenEffect.Message(
                           message = messageResId
                       )
                   )
               }
           }
       }
   }

    private suspend fun validateExpenseForm(
        title: String,
    ): Boolean {
        var errorCounts = 0
        if (!ValidatorHelper.validateExpenseTitle(title)) {
            _pageEffect.emit(AddPlannedExpenseScreenEffect.ErrorDescriptionInput)
            errorCounts++
        }
        return errorCounts == 0
    }
}