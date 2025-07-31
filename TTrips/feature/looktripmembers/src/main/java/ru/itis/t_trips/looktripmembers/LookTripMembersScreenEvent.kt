package ru.itis.t_trips.looktripmembers

internal sealed interface LookTripMembersScreenEvent {
    data class OnScreenInit(val tripId: Int) : LookTripMembersScreenEvent
    data object OnBackBtnClick : LookTripMembersScreenEvent
}