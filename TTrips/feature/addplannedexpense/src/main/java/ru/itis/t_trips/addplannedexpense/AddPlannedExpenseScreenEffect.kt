package ru.itis.t_trips.addplannedexpense

import androidx.annotation.StringRes

internal sealed interface AddPlannedExpenseScreenEffect {
    data class Message(@StringRes val message: Int) : AddPlannedExpenseScreenEffect
    data object ErrorDescriptionInput : AddPlannedExpenseScreenEffect
    data object ErrorAmountInput : AddPlannedExpenseScreenEffect
}