package ru.itis.t_trips.network.model.response.expense

import com.google.gson.annotations.SerializedName

class ParticipantsResponse(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("expenseId")
    val expenseId: Int?,

    @SerializedName("participantId")
    val participantId: Int?,

    @SerializedName("paidByUserId")
    val paidByUserId: Int?,

    @SerializedName("amount")
    val amount: Double?
)