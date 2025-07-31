package ru.itis.t_trips.network.model.response.expense

import com.google.gson.annotations.SerializedName

class PlannedExpenseResponse (
    @SerializedName("id")
    val id: Int?,

    @SerializedName("header")
    val title: String?,

    @SerializedName("tripId")
    val tripId: Int?,

    @SerializedName("amount")
    val amount: Double?,

    @SerializedName("category")
    val category: String?,
)