package ru.itis.t_trips.tripinfo

import ru.itis.t_trips.ui.model.enums.ExpenseCategory
import ru.itis.t_trips.ui.model.enums.ExpenseTab

internal sealed interface TripInfoScreenEvent {
    data class OnInitScreen(val tripId: Int): TripInfoScreenEvent
    data object OnBackBtnClick: TripInfoScreenEvent
    data class OnEditBtnClick(val tripId: Int): TripInfoScreenEvent
    data class OnLookMembersBtnClick(val tripId: Int): TripInfoScreenEvent
    data class OnTabSelected(val tab: ExpenseTab) : TripInfoScreenEvent
    data class OnTabCategorySelected(val tab: ExpenseCategory) : TripInfoScreenEvent
    data class OnFinishBtnClick(val tripId: Int): TripInfoScreenEvent
    data class OnActualExpenseItemClick(val expenseId: Int, val tripId: Int): TripInfoScreenEvent
    data class OnAddActualExpense(val tripId: Int) : TripInfoScreenEvent
    data class OnAddPlannedExpense(val tripId: Int) : TripInfoScreenEvent
    data class OnDeletePlannedExpenseIconClick(val expenseId: Int) : TripInfoScreenEvent
}