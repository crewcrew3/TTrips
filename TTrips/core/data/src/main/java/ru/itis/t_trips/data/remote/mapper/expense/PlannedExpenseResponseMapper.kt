package ru.itis.t_trips.data.remote.mapper.expense

import ru.itis.t_trips.domain.model.expense.PlannedExpenseModel
import ru.itis.t_trips.network.model.response.expense.PlannedExpenseResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class PlannedExpenseResponseMapper @Inject constructor() {
    fun map(input: PlannedExpenseResponse?): PlannedExpenseModel {
        val response = requireNotNull(input) { ExceptionCode.EXPENSE_PLANNED_RESPONSE }
        return PlannedExpenseModel(
            id = requireNotNull(response.id) { ExceptionCode.EXPENSE_PLANNED_RESPONSE },
            title = requireNotNull(response.title) { ExceptionCode.EXPENSE_PLANNED_RESPONSE },
            tripId = requireNotNull(response.tripId) { ExceptionCode.EXPENSE_PLANNED_RESPONSE },
            category = requireNotNull(response.category) { ExceptionCode.EXPENSE_PLANNED_RESPONSE },
            amount = requireNotNull(response.amount) { ExceptionCode.EXPENSE_PLANNED_RESPONSE },
        )
    }
}