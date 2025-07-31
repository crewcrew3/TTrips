package ru.itis.t_trips.triparchive

import androidx.annotation.StringRes

internal sealed interface TripArchiveEffect {
    data class Error(@StringRes val message: Int) : TripArchiveEffect
}