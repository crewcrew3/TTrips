package ru.itis.t_trips.actualexpenseinfo

import ru.itis.t_trips.domain.model.expense.ActualExpenseModel
import ru.itis.t_trips.ui.model.Contact

internal sealed interface ActualExpenseInfoScreenState {
    data object Initial : ActualExpenseInfoScreenState
    data object Loading : ActualExpenseInfoScreenState
    data class Result(
        val expense: ActualExpenseModel,
        val picUrl: String?,
        val userPaid: Contact,
        val participants: List<Contact>,
        val isCreator: Boolean,
        ) : ActualExpenseInfoScreenState
}