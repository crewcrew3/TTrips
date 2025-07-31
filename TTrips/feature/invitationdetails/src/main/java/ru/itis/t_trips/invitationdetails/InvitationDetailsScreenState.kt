package ru.itis.t_trips.invitationdetails

import ru.itis.t_trips.domain.model.InvitationModel

internal sealed interface InvitationDetailsScreenState {
    data object Initial : InvitationDetailsScreenState
    data object Loading : InvitationDetailsScreenState
    data class Result(val invitation: InvitationModel, val tripPicUrl: String?) : InvitationDetailsScreenState
}