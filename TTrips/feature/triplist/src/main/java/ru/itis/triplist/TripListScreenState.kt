package ru.itis.triplist

import ru.itis.t_trips.ui.model.TripWithPicture

internal sealed interface TripListScreenState {
    data object Initial : TripListScreenState
    data object Loading : TripListScreenState
    data class TripsResult(val result: List<TripWithPicture>) : TripListScreenState
}