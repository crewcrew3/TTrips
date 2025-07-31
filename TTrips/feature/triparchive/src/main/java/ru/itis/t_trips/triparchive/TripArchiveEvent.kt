package ru.itis.t_trips.triparchive

internal sealed interface TripArchiveEvent {
    data object OnScreenInit : TripArchiveEvent
    data class OnDeleteTripBtnClick(val tripId: Int) : TripArchiveEvent
    data object OnBackBtnClick: TripArchiveEvent
    data class OnItemClick(val tripId: Int): TripArchiveEvent
}