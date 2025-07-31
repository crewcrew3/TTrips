package ru.itis.t_trips.network.model.response.expense

import com.google.gson.annotations.SerializedName

class ActualExpenseResponse (
    @SerializedName("id")
    val id: Int?,

    @SerializedName("description")
    val title: String?,

    @SerializedName("totalAmount")
    val totalAmount: Double?,

    @SerializedName("tripId")
    val tripId: Int?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("paidByUserId")
    val paidByUserId: Int?,

    @SerializedName("participants")
    val participants: List<ParticipantsResponse?>?
)