package ru.itis.t_trips.addactualexpense.addexpense.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.t_trips.addactualexpense.addexpense.AddActualExpenseScreenEffect
import ru.itis.t_trips.addactualexpense.addexpense.AddActualExpenseScreenEvent
import ru.itis.t_trips.addactualexpense.addexpense.AddActualExpenseScreenState
import ru.itis.t_trips.addactualexpense.addexpense.ActualExpenseFormState
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.SavePictureUseCase
import ru.itis.t_trips.domain.usecase.expense.SaveActualExpenseUseCase
import ru.itis.t_trips.domain.usecase.trip.GetTripMembersUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.utils.OtherProperties
import ru.itis.t_trips.utils.ValidatorHelper
import javax.inject.Inject

@HiltViewModel
internal class AddActualExpenseViewModel @Inject constructor(
    private val getTripMembersUseCase: GetTripMembersUseCase,
    private val saveActualExpenseUseCase: SaveActualExpenseUseCase,
    private val savePictureUseCase: SavePictureUseCase,
    private val tripNavigator: TripNavigator,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<AddActualExpenseScreenState>(value = AddActualExpenseScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = ActualExpenseFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<AddActualExpenseScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: AddActualExpenseScreenEvent) {
        when (event) {
            is AddActualExpenseScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is AddActualExpenseScreenEvent.OnScreenInit -> getTripMembers(event.tripId)
            is AddActualExpenseScreenEvent.OnDivideBtnClick -> tripNavigator.toDivideExpenseScreen(
                participants = Json.encodeToString(event.participants),
                tripId = event.tripId,
                totalAmount = event.totalAmount
            )
            is AddActualExpenseScreenEvent.OnSaveBtnClick -> saveActualExpense(
                tripId = event.tripId,
                title = event.title,
                category = event.category,
                participants = event.participantsStr,
                uri = event.uri,
                context = event.context
            )
            is AddActualExpenseScreenEvent.OnFormFieldChanged -> {
                _formState.update { state ->
                    state.copy(
                        description = event.description ?: state.description,
                        amount = event.amount ?: state.amount,
                        category = event.category ?: state.category,
                        imageUri = event.imageUri ?: state.imageUri,
                        participants = event.participants ?: state.participants
                    )
                }
            }
        }
    }

    private fun saveActualExpense(
        tripId: Int,
        title: String,
        category: String,
        participants: String,
        uri: Uri?,
        context: Context?
    ) {
        viewModelScope.launch {
            if (validateExpenseForm(title = title)) {
                runCatching {
                    val participantsMap: Map<Int, Double> = Json.decodeFromString(participants)
                    saveActualExpenseUseCase(
                        tripId = tripId,
                        title = title,
                        category = category,
                        participants = participantsMap
                    )
                }.onSuccess { expenseId ->
                    if (uri != null && context != null) {
                        savePicture(context, uri, expenseId)
                    }
                    _pageEffect.emit(
                        AddActualExpenseScreenEffect.Message(
                            message = R.string.msg_save_data_success
                        )
                    )
                    tripNavigator.toActualExpenseInfoScreen(expenseId = expenseId, tripId = tripId)
                }.onFailure { exception ->
                    val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                    _pageEffect.emit(
                        AddActualExpenseScreenEffect.Message(
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
            _pageEffect.emit(AddActualExpenseScreenEffect.ErrorDescriptionInput)
            errorCounts++
        }
        return errorCounts == 0
    }

    private fun savePicture(context: Context, sourceUri: Uri, imageId: Int) {
        viewModelScope.launch {
            runCatching {
                savePictureUseCase(
                    keyPrefix = OtherProperties.FILE_EXPENSE_PIC_PREFIX,
                    context = context,
                    sourceUri = sourceUri,
                    imageId = imageId
                )
            }.onFailure {
                _pageEffect.emit(
                    AddActualExpenseScreenEffect.Message(
                        R.string.exception_msg_picture
                    )
                )
            }
        }
    }

    private fun getTripMembers(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                getTripMembersUseCase(tripId)
            }.onSuccess { result ->
                val members = result.map { userModel ->
                    Contact(
                        id = userModel.id,
                        name = userModel.firstName + " " + userModel.lastName,
                    )
                }
                _pageState.value = AddActualExpenseScreenState.MembersResult(members = members)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    AddActualExpenseScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}