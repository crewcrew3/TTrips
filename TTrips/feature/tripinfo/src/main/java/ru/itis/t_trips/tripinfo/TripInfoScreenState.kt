package ru.itis.t_trips.tripinfo

import ru.itis.t_trips.domain.model.TripModel
import ru.itis.t_trips.domain.model.expense.ExpenseModel
import ru.itis.t_trips.ui.model.enums.ExpenseCategory
import ru.itis.t_trips.ui.model.enums.ExpenseTab

internal sealed interface TripInfoScreenState {
    data object Initial : TripInfoScreenState
    data object Loading : TripInfoScreenState
    data class TripInfoResult(
        val result: TripModel,
        val picUrl: String?,
        val isCreator: Boolean,
        val selectedTab: ExpenseTab = ExpenseTab.PLANNED,
        val selectedCategoryTab: ExpenseCategory = ExpenseCategory.FLIGHT,
        val plannedExpenses: List<ExpenseModel>,
        val actualExpenses: List<ExpenseModel>,
    ) : TripInfoScreenState
}