package ru.itis.t_trips.invitationdetails.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.GetPictureUseCase
import ru.itis.t_trips.domain.usecase.invitation.AcceptInvitationUseCase
import ru.itis.t_trips.domain.usecase.invitation.GetInvitationUseCase
import ru.itis.t_trips.domain.usecase.invitation.RejectInvitationUseCase
import ru.itis.t_trips.invitationdetails.InvitationDetailsScreenEffect
import ru.itis.t_trips.invitationdetails.InvitationDetailsScreenEvent
import ru.itis.t_trips.invitationdetails.InvitationDetailsScreenState
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
internal class InvitationDetailsViewModel @Inject constructor(
    private val getInvitationUseCase: GetInvitationUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val rejectInvitationUseCase: RejectInvitationUseCase,
    private val getPictureUseCase: GetPictureUseCase,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<InvitationDetailsScreenState>(value = InvitationDetailsScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<InvitationDetailsScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: InvitationDetailsScreenEvent) {
        when (event) {
            is InvitationDetailsScreenEvent.OnScreenInit -> loadInvitation(
                id = event.id,
                userId = event.userId,
                tripId = event.tripId
            )
            is InvitationDetailsScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is InvitationDetailsScreenEvent.OnLookMembersBtnClick -> navigator.toMembersTripScreen(event.tripId)
            is InvitationDetailsScreenEvent.OnAcceptBtnClickInvitation -> acceptInvitation(event.invitationId)
            is InvitationDetailsScreenEvent.OnRejectBtnClickInvitation -> rejectInvitation(event.invitationId)
        }
    }

    private fun loadInvitation(id: Int, userId: Int, tripId: Int) {
        viewModelScope.launch {
            runCatching {
                _pageState.value = InvitationDetailsScreenState.Loading
                //delay(2000)
                getInvitationUseCase(
                    id = id,
                    userId = userId,
                    tripId = tripId
                )
            }.onSuccess { invitation ->
                val tripPicUrl = getPictureUseCase(
                    keyPrefix = OtherProperties.FILE_TRIP_PIC_PREFIX,
                    imageId = invitation.trip.id
                )
                _pageState.value = InvitationDetailsScreenState.Result(invitation = invitation, tripPicUrl = tripPicUrl)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    InvitationDetailsScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun acceptInvitation(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                acceptInvitationUseCase(tripId)
            }.onSuccess {
                _pageEffect.emit(
                    InvitationDetailsScreenEffect.Message(
                        message = R.string.msg_accept_invitation_success
                    )
                )
                navigator.popBackStack()
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    InvitationDetailsScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun rejectInvitation(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                rejectInvitationUseCase(tripId)
            }.onSuccess {
                _pageEffect.emit(
                    InvitationDetailsScreenEffect.Message(
                        message = R.string.msg_reject_invitation_success
                    )
                )
                navigator.popBackStack()
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    InvitationDetailsScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}