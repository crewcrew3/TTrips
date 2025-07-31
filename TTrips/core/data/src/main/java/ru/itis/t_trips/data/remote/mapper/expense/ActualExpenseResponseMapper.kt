package ru.itis.t_trips.data.remote.mapper.expense

import ru.itis.t_trips.domain.model.expense.ActualExpenseModel
import ru.itis.t_trips.network.model.response.expense.ActualExpenseResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class ActualExpenseResponseMapper @Inject constructor() {

    fun map(input: ActualExpenseResponse?): ActualExpenseModel {

        val response = requireNotNull(input) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE }

        val participantsMap = requireNotNull(response.participants) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE }.associate { item ->
            requireNotNull(item) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE }
            requireNotNull(item.participantId) to requireNotNull(item.amount)
        }
        return ActualExpenseModel(
            id = requireNotNull(response.id) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE },
            title = requireNotNull(response.title) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE },
            tripId = requireNotNull(response.tripId) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE },
            category = requireNotNull(response.category) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE },
            paidByUserId = requireNotNull(response.paidByUserId) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE },
            participants = participantsMap,
            amount = requireNotNull(response.totalAmount) { ExceptionCode.EXPENSE_ACTUAL_RESPONSE },
        )
    }
}