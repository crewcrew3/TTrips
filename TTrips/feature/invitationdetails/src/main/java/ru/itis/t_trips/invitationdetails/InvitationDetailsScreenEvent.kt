package ru.itis.t_trips.invitationdetails

internal sealed interface InvitationDetailsScreenEvent {
    data class OnScreenInit(val id: Int, val userId: Int, val tripId: Int) : InvitationDetailsScreenEvent
    data object OnBackBtnClick : InvitationDetailsScreenEvent
    data class OnLookMembersBtnClick(val tripId: Int) : InvitationDetailsScreenEvent
    data class OnAcceptBtnClickInvitation(val invitationId: Int) : InvitationDetailsScreenEvent
    data class OnRejectBtnClickInvitation(val invitationId: Int) : InvitationDetailsScreenEvent
}