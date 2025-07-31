package ru.itis.t_trips.domain.repository

import ru.itis.t_trips.domain.model.expense.ActualExpenseModel
import ru.itis.t_trips.domain.model.expense.PlannedExpenseModel

interface ExpenseRepository {
    suspend fun getPlannedExpenses(tripId: Int): List<PlannedExpenseModel>
    suspend fun getActualExpenses(tripId: Int): List<ActualExpenseModel>
    suspend fun saveActualExpense(
        tripId: Int,
        title: String,
        category: String,
        participants: Map<Int, Double>
    ) : Int
    suspend fun getActualExpenseById(expenseId: Int): ActualExpenseModel
    suspend fun savePlannedExpense(
        tripId: Int,
        title: String,
        amount: String,
        category: String,
    ): Int
    suspend fun deleteActualExpense(expenseId: Int)
    suspend fun deletePlannedExpense(expenseId: Int)
}