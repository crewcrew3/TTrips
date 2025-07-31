package ru.itis.t_trips.addtrip.commoninfo.ui

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
import kotlinx.serialization.json.Json
import ru.itis.t_trips.addtrip.commoninfo.AddTripFormState
import ru.itis.t_trips.addtrip.commoninfo.AddTripScreenEffect
import ru.itis.t_trips.addtrip.commoninfo.AddTripScreenEvent
import ru.itis.t_trips.addtrip.commoninfo.AddTripScreenState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.domain.usecase.trip.AddTripUseCase
import ru.itis.t_trips.domain.usecase.user.GetUsersByPhoneNumberListUseCase
import ru.itis.t_trips.domain.usecase.invitation.InviteMemberForTripUseCase
import ru.itis.t_trips.domain.usecase.SavePictureUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.utils.OtherProperties
import ru.itis.t_trips.utils.ValidatorHelper
import javax.inject.Inject

@HiltViewModel
internal class AddTripViewModel @Inject constructor(
    private val addTripUseCase: AddTripUseCase,
    private val savePictureUseCase: SavePictureUseCase,
    private val getUsersByPhoneNumberListUseCase: GetUsersByPhoneNumberListUseCase,
    private val inviteMemberForTripUseCase: InviteMemberForTripUseCase,
    private val tripNavigator: TripNavigator,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<AddTripScreenState>(value = AddTripScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = AddTripFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<AddTripScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: AddTripScreenEvent, context: Context? = null) {
        when (event) {
            is AddTripScreenEvent.OnAddMembersBtnClick -> tripNavigator.toContactsScreen()
            is AddTripScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is AddTripScreenEvent.OnSaveBtnClick -> {
                addTrip(
                    title = event.title,
                    startDate = event.startDate,
                    endDate = event.endDate,
                    budget = event.budget,
                    uri = event.uri,
                    context = context,
                    membersList = event.membersList
                )
            }
            is AddTripScreenEvent.OnAddMembers -> checkMembers(contactsStr = event.contacts)
            is AddTripScreenEvent.OnRemoveContact -> removeContact(event.contact)
            is AddTripScreenEvent.OnFormFieldChanged -> {
                _formState.update { state ->
                    state.copy(
                        title = event.title ?: state.title,
                        startDate = event.startDate ?: state.startDate,
                        endDate = event.endDate ?: state.endDate,
                        startDateUi = event.startDateUi ?: state.startDateUi,
                        endDateUi = event.endDateUi ?: state.endDateUi,
                        budget = event.budget ?: state.budget,
                        uri = event.uri ?: state.uri,
                    )
                }
            }
        }
    }

    private fun addTrip(
        title: String,
        startDate: String,
        endDate: String,
        budget: String,
        uri: Uri?,
        context: Context?,
        membersList: List<Contact>
    ) {
        viewModelScope.launch {
            if (validateTripForm(title = title, budget = budget)) {
                runCatching {
                    addTripUseCase(
                        title = title,
                        startDate = startDate,
                        endDate = endDate,
                        budget = budget.toDouble(),
                    )
                }.onSuccess { tripModel ->
                    if (uri != null && context != null) {
                        savePicture(context, uri, tripModel.id)
                    }
                    inviteMembers(tripId = tripModel.id, membersList = membersList)
                    tripNavigator.toTripDetailsScreen(tripModel.id)
                }.onFailure { exception ->
                    val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                    _pageEffect.emit(
                        AddTripScreenEffect.Message(
                            message = messageResId
                        )
                    )
                }
            }
        }
    }

    private suspend fun validateTripForm(
        title: String,
        budget: String,
    ): Boolean {
        var errorCounts = 0
        if (!ValidatorHelper.validateTripTitle(title)) {
            _pageEffect.emit(AddTripScreenEffect.ErrorTripTitleInput)
            errorCounts++
        }
        if (budget.isBlank()) {
            _pageEffect.emit(AddTripScreenEffect.ErrorTripBudgetInput)
            errorCounts++
        }
        return errorCounts == 0
    }

    private fun savePicture(context: Context, sourceUri: Uri, imageId: Int) {
        viewModelScope.launch {
            runCatching {
                savePictureUseCase(
                    keyPrefix = OtherProperties.FILE_TRIP_PIC_PREFIX,
                    context = context,
                    sourceUri = sourceUri,
                    imageId = imageId
                )
            }.onFailure {
                _pageEffect.emit(
                    AddTripScreenEffect.Message(
                        R.string.exception_msg_picture
                    )
                )
            }
        }
    }

    private fun inviteMembers(tripId: Int, membersList: List<Contact>) {
        viewModelScope.launch {
            runCatching {
                val phoneNumbers = membersList.map { it.phoneNumber }
                inviteMemberForTripUseCase(tripId = tripId, phoneNumbers = phoneNumbers)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    AddTripScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun checkMembers(contactsStr: String) {
//        val mockList: List<Contact> = Json.decodeFromString(contactsStr)
//        _pageState.value = AddTripScreenState.Result(contacts = mockList)
        viewModelScope.launch {
            runCatching {
                val contacts: List<Contact> = Json.decodeFromString(contactsStr)
                contacts
                //когда на бэке эндпоинт появится
//                val phoneNumbers = contacts.map { it.phoneNumber }
//                getUsersByPhoneNumberListUseCase(phoneNumbers).map {userProfileModel ->
//                    Contact(
//                        id = userProfileModel.id,
//                        name = userProfileModel.firstName + " " + userProfileModel.lastName,
//                        phoneNumber = userProfileModel.phoneNumber
//                    )
//                }
            }.onSuccess { result ->
                _pageState.value = AddTripScreenState.Result(contacts = result)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    AddTripScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun removeContact(contact: Contact) {
        (_pageState.value as? AddTripScreenState.Result)?.let { resultState ->
            val currentContacts = resultState.contacts
            val updatedContacts = currentContacts.filter { it != contact }
            _pageState.value = AddTripScreenState.Result(contacts = updatedContacts)
        }
    }
}