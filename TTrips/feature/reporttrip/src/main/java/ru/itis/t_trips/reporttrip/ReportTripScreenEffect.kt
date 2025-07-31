package ru.itis.t_trips.reporttrip

import androidx.annotation.StringRes

internal sealed interface ReportTripScreenEffect {
    data class Message(@StringRes val message: Int) : ReportTripScreenEffect
}