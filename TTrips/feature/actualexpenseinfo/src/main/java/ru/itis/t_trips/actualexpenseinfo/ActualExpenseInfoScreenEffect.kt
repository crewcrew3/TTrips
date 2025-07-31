package ru.itis.t_trips.actualexpenseinfo

import androidx.annotation.StringRes

internal sealed interface ActualExpenseInfoScreenEffect {
    data class Message(@StringRes val message: Int) : ActualExpenseInfoScreenEffect
}