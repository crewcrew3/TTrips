package ru.itis.t_trips.ui.model

data class TripWithPicture(
    val id: Int,
    val title: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val budget: Double,
    val picUrl: String?,
)