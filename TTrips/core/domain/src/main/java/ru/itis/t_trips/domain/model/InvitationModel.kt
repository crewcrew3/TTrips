package ru.itis.t_trips.domain.model

class InvitationModel(
    val id: Int,
    val trip: TripModel,
    val inviterUser: UserProfileModel,
)