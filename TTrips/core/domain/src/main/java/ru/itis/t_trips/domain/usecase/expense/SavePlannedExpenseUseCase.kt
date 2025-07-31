package ru.itis.t_trips.domain.usecase.expense

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.ExpenseRepository
import javax.inject.Inject

class SavePlannedExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        tripId: Int,
        title: String,
        amount: String,
        category: String,
    ): Int {
        return withContext(dispatcher) {
            expenseRepository.savePlannedExpense(
                tripId = tripId,
                title = title,
                amount = amount,
                category = category,
            )
        }
    }
}