package ru.itis.t_trips.domain.model

data class UserProfileModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
)