package ru.itis.t_trips.addactualexpense.divideexpense

import androidx.annotation.StringRes

internal sealed interface DivideExpenseScreenEffect {
    data class Message(@StringRes val message: Int) : DivideExpenseScreenEffect
}