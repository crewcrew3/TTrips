package ru.itis.t_trips.addactualexpense.addexpense

import androidx.annotation.StringRes

internal sealed interface AddActualExpenseScreenEffect {
    data class Message(@StringRes val message: Int) : AddActualExpenseScreenEffect
    data object ErrorDescriptionInput : AddActualExpenseScreenEffect
    data object ErrorAmountInput : AddActualExpenseScreenEffect
}