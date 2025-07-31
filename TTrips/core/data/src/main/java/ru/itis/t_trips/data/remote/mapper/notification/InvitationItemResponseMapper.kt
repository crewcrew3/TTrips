package ru.itis.t_trips.data.remote.mapper.notification

import ru.itis.t_trips.domain.model.notification.InvitationItemModel
import ru.itis.t_trips.network.model.response.invitation.InvitationResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

class InvitationItemResponseMapper @Inject constructor() {
    fun map(input: InvitationResponse?) : InvitationItemModel {
        val invitationResponse = requireNotNull(input) { ExceptionCode.INVITATION_RESPONSE }
        return InvitationItemModel(
            id = requireNotNull(invitationResponse.id) { ExceptionCode.INVITATION_RESPONSE },
            tripId = requireNotNull(invitationResponse.tripId) { ExceptionCode.INVITATION_RESPONSE },
            inviterId = requireNotNull(invitationResponse.inviterId) { ExceptionCode.INVITATION_RESPONSE },
        )
    }
}