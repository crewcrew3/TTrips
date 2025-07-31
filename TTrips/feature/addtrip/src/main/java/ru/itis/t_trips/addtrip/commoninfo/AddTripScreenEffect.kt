package ru.itis.t_trips.addtrip.commoninfo

import androidx.annotation.StringRes

internal sealed interface AddTripScreenEffect {
    data class Message(@StringRes val message: Int) : AddTripScreenEffect
    data object ErrorTripTitleInput : AddTripScreenEffect
    data object ErrorTripBudgetInput : AddTripScreenEffect
}