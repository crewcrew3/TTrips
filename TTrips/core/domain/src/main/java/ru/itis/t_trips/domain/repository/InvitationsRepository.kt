package ru.itis.t_trips.domain.repository

import ru.itis.t_trips.domain.model.InvitationModel
import ru.itis.t_trips.domain.model.notification.InvitationItemModel

interface InvitationsRepository {
    suspend fun getUsersInvitations() : List<InvitationItemModel>
    suspend fun getInvitationInfo(invitationId: Int, userId: Int, tripId: Int): InvitationModel
    suspend fun acceptInvitation(invitationId: Int)
    suspend fun rejectInvitation(invitationId: Int)
}