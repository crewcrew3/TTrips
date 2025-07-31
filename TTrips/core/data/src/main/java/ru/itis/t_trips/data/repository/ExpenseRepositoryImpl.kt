package ru.itis.t_trips.data.repository

import retrofit2.HttpException
import ru.itis.t_trips.data.remote.mapper.expense.ActualExpenseResponseMapper
import ru.itis.t_trips.data.remote.mapper.expense.PlannedExpenseResponseMapper
import ru.itis.t_trips.domain.model.expense.ActualExpenseModel
import ru.itis.t_trips.domain.model.expense.PlannedExpenseModel
import ru.itis.t_trips.domain.repository.ExpenseRepository
import ru.itis.t_trips.network.api.ExpenseApi
import ru.itis.t_trips.network.model.request.expense.ActualExpenseRequest
import ru.itis.t_trips.network.model.request.expense.ParticipantsRequest
import ru.itis.t_trips.network.model.request.expense.PlannedExpenseRequest
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.runCatchingNonCancellable
import java.net.HttpURLConnection
import javax.inject.Inject

internal class ExpenseRepositoryImpl @Inject constructor(
    private val expenseApi: ExpenseApi,
    private val plannedExpenseResponseMapper: PlannedExpenseResponseMapper,
    private val actualExpenseResponseMapper: ActualExpenseResponseMapper,
) : ExpenseRepository {

    override suspend fun saveActualExpense(
        tripId: Int,
        title: String,
        category: String,
        participants: Map<Int, Double>
    ) : Int {
        return runCatchingNonCancellable {
            val newExpense = expenseApi.saveActualExpense(
                tripId = tripId,
                request = ActualExpenseRequest(
                    title = title,
                    category = category,
                    participants = participants.map { entry ->
                        ParticipantsRequest(
                            participantId = entry.key,
                            amount = entry.value
                        )
                    }
                )
            )
            requireNotNull(newExpense?.id) { ExceptionCode.EXPENSE_SAVE_ACTUAL_RESPONSE }
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw IllegalArgumentException(
                            ExceptionCode.INVALID_DATA)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun getActualExpenses(tripId: Int): List<ActualExpenseModel> {
        return runCatchingNonCancellable {
            expenseApi.getActualExpenses(tripId).map(actualExpenseResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun getActualExpenseById(expenseId: Int): ActualExpenseModel {
        return runCatchingNonCancellable {
            expenseApi.getActualExpense(expenseId = expenseId).let(actualExpenseResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun deleteActualExpense(expenseId: Int) {
        runCatchingNonCancellable {
            expenseApi.deleteActualExpense(expenseId)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }

        }
    }

    override suspend fun deletePlannedExpense(expenseId: Int) {
        runCatchingNonCancellable {
            expenseApi.deletePlannedExpense(expenseId)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }

        }
    }

    override suspend fun savePlannedExpense(
        tripId: Int,
        title: String,
        amount: String,
        category: String
    ) : Int {
        return runCatchingNonCancellable {
            val newExpense = expenseApi.savePlannedExpense(
                tripId = tripId,
                request = PlannedExpenseRequest(
                    title = title,
                    amount = amount,
                    category = category,
                ),
            )
            requireNotNull(newExpense?.id) { ExceptionCode.EXPENSE_SAVE_ACTUAL_RESPONSE }
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw IllegalArgumentException(
                            ExceptionCode.INVALID_DATA)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }

        }.getOrThrow()
    }

    override suspend fun getPlannedExpenses(tripId: Int): List<PlannedExpenseModel> {
        return runCatchingNonCancellable {
            expenseApi.getPlannedExpenses(tripId).map(plannedExpenseResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(
                            ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(
                            ExceptionCode.NOT_FOUND
                        )
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(
                            ExceptionCode.FORBIDDEN
                        )
                    }
                }
            }
        }.getOrThrow()
    }
}