package ru.itis.t_trips.network.model.request.expense

import com.google.gson.annotations.SerializedName

class PlannedExpenseRequest(
    @SerializedName("header")
    val title: String,

    @SerializedName("amount")
    val amount: String,

    @SerializedName("category")
    val category: String,
)