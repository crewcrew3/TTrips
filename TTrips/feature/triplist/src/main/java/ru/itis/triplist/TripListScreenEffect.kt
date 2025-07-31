package ru.itis.triplist

import androidx.annotation.StringRes

internal sealed interface TripListScreenEffect {
    data class Error(@StringRes val message: Int) : TripListScreenEffect
}
