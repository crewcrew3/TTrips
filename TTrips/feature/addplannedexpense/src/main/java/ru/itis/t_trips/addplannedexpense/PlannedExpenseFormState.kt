package ru.itis.t_trips.addplannedexpense

import ru.itis.t_trips.ui.model.enums.ExpenseCategory

data class PlannedExpenseFormState(
    val title: String = "",
    val amount: String = "",
    val category: String = ExpenseCategory.FLIGHT.toString(),
)