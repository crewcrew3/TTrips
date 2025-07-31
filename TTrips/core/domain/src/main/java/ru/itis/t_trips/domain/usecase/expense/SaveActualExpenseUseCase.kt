package ru.itis.t_trips.domain.usecase.expense

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.ExpenseRepository
import javax.inject.Inject

class SaveActualExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        tripId: Int,
        title: String,
        category: String,
        participants: Map<Int, Double>
    ): Int {
        return withContext(dispatcher) {
            expenseRepository.saveActualExpense(
                tripId = tripId,
                title = title,
                category = category,
                participants = participants
            )
        }
    }
}