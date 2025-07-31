package ru.itis.t_trips.domain.model.expense

open class ExpenseModel(
    val id: Int,
    val title: String,
    val tripId: Int,
    val category: String,
    val amount: Double,
)