package ru.itis.t_trips.domain.usecase.expense

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.expense.PlannedExpenseModel
import ru.itis.t_trips.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetPlannedExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int): List<PlannedExpenseModel> {
        return withContext(dispatcher) {
            expenseRepository.getPlannedExpenses(tripId)
        }
    }
}