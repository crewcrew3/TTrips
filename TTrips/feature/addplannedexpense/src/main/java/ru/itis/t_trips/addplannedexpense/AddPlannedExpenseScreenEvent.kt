package ru.itis.t_trips.addplannedexpense

internal sealed interface AddPlannedExpenseScreenEvent {
    data object OnBackBtnClick : AddPlannedExpenseScreenEvent
    data class OnSaveBtnClick(
        val tripId: Int,
        val title: String,
        val amount: String,
        val category: String,
    ) : AddPlannedExpenseScreenEvent
    data class OnFormFieldChanged(
        val title: String? = null,
        val amount: String? = null,
        val category: String? = null,
    ) : AddPlannedExpenseScreenEvent
}