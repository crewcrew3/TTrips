package ru.itis.t_trips.addactualexpense.addexpense

import android.net.Uri
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.ui.model.enums.ExpenseCategory

data class ActualExpenseFormState(
    val description: String = "",
    val amount: String = "",
    val category: String = ExpenseCategory.FLIGHT.toString(),
    val imageUri: Uri? = null,
    val participants: List<Contact> = emptyList()
)