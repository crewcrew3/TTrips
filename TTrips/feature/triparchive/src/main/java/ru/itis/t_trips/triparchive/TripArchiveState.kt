package ru.itis.t_trips.triparchive

import ru.itis.t_trips.ui.model.TripWithPicture

internal sealed interface TripArchiveState {
    data object Initial : TripArchiveState
    data object Loading : TripArchiveState
    data class TripsResult(val result: List<TripWithPicture>) : TripArchiveState
}