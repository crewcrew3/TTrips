package ru.itis.t_trips.addactualexpense.addexpense

import ru.itis.t_trips.ui.model.Contact

internal sealed interface AddActualExpenseScreenState {
    data object Initial : AddActualExpenseScreenState
    data class MembersResult(val members: List<Contact>) : AddActualExpenseScreenState
}