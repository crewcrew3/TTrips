package ru.itis.t_trips.reporttrip

internal sealed interface ReportTripScreenEvent {
    data class OnScreenInit(val tripId: Int) : ReportTripScreenEvent
    data class OnUserCardClick(val userId: Int, val tripId: Int) : ReportTripScreenEvent
    data object OnBackBtnClick : ReportTripScreenEvent
}