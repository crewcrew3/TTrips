package ru.itis.t_trips.domain.model.notification

class InvitationItemModel(
    id: Int,
    tripId: Int,
    val inviterId: Int,
) : NotificationModel(
    id = id,
    tripId = tripId
)