package ru.itis.triplist

internal sealed interface TripListScreenEvent {
    data object OnScreenInit : TripListScreenEvent
    data class OnItemClick(val tripId: Int) : TripListScreenEvent
    data object OnAddTripBtnClick : TripListScreenEvent
}