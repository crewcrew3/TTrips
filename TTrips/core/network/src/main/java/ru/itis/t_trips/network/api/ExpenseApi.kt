package ru.itis.t_trips.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.itis.t_trips.network.model.request.expense.ActualExpenseRequest
import ru.itis.t_trips.network.model.request.expense.PlannedExpenseRequest
import ru.itis.t_trips.network.model.response.expense.ActualExpenseResponse
import ru.itis.t_trips.network.model.response.expense.PlannedExpenseResponse

interface ExpenseApi {

    @POST("expenses/{tripId}")
    suspend fun saveActualExpense(
        @Path("tripId") tripId: Int,
        @Body request: ActualExpenseRequest,
    ) : ActualExpenseResponse?

    @GET("expenses/{expenseId}")
    suspend fun getActualExpense(@Path("expenseId") expenseId: Int) : ActualExpenseResponse?

    @GET("expenses/trip/{tripId}")
    suspend fun getActualExpenses(@Path("tripId") tripId: Int): List<ActualExpenseResponse?>

    @DELETE("expenses/{expenseId}")
    suspend fun deleteActualExpense(@Path("expenseId") expenseId: Int)

    @POST("plan/expenses/{tripId}")
    suspend fun savePlannedExpense(
        @Path("tripId") tripId: Int,
        @Body request: PlannedExpenseRequest,
    ) : PlannedExpenseResponse?

    @GET("plan/expenses/{tripId}")
    suspend fun getPlannedExpenses(@Path("tripId") tripId: Int): List<PlannedExpenseResponse?>

    @DELETE("plan/expenses/{expenseId}")
    suspend fun deletePlannedExpense(@Path("expenseId") expenseId: Int)
}