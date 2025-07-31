package ru.itis.t_trips.domain.model.expense

class ActualExpenseModel(
    id: Int,
    title: String,
    tripId: Int,
    category: String,
    amount: Double,
    val paidByUserId: Int,
    val participants: Map<Int, Double>
) : ExpenseModel(
    id = id,
    title = title,
    tripId = tripId,
    category = category,
    amount = amount
)