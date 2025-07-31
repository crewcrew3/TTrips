package ru.itis.t_trips.data.remote.mapper

import ru.itis.t_trips.domain.model.InvitationModel
import ru.itis.t_trips.network.model.response.trip.TripResponse
import ru.itis.t_trips.network.model.response.user.UserProfileResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class InvitationResponseMapper @Inject constructor(
    private val userProfileResponseMapper: UserProfileResponseMapper,
    private val tripResponseMapper: TripResponseMapper,
) {
    fun map(
        invitationId: Int?,
        inputUser: UserProfileResponse?,
        inputTrip: TripResponse?
    ): InvitationModel {
        return InvitationModel(
            id = requireNotNull(invitationId) { ExceptionCode.INVITATION_RESPONSE },
            inviterUser = userProfileResponseMapper.map(inputUser),
            trip = tripResponseMapper.map(inputTrip)
        )
    }
}