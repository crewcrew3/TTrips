package ru.itis.t_trips.domain.model

data class TripModel(
    val id: Int,
    val title: String,
    val status: String,
    val startDate: String,
    val endDate: String,
    val budget: Double,
)