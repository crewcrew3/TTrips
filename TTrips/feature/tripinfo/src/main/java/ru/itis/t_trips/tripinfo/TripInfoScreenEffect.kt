package ru.itis.t_trips.tripinfo

import androidx.annotation.StringRes

internal sealed interface TripInfoScreenEffect {
    data class Message(@StringRes val message: Int) : TripInfoScreenEffect
}