package ru.itis.t_trips.addactualexpense.divideexpense

internal sealed interface DivideExpenseScreenEvent {
    data object OnBackBtnClick : DivideExpenseScreenEvent
    data class OnNextBtnClick(val tripId: Int, val wayToDivideAmount: String, val participants: Map<Int, Double>) : DivideExpenseScreenEvent
}