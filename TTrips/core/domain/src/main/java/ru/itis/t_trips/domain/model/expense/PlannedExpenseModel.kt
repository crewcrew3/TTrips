package ru.itis.t_trips.domain.model.expense

class PlannedExpenseModel(
    id: Int,
    title: String,
    tripId: Int,
    category: String,
    amount: Double,
) : ExpenseModel(
    id = id,
    title = title,
    tripId = tripId,
    category = category,
    amount = amount
)