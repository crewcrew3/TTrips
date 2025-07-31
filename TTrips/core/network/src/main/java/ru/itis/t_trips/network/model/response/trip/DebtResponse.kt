package ru.itis.t_trips.network.model.response.trip

import com.google.gson.annotations.SerializedName

class DebtResponse(
    @SerializedName("tripId")
    val tripId: Int?,

    @SerializedName("amount")
    val amount: Double?,

    @SerializedName("debtorId")
    val debtorId: Int?,

    @SerializedName("creditorId")
    val creditorId: Int?,
)