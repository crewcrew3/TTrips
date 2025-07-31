package ru.itis.t_trips.network.model.request.expense

import com.google.gson.annotations.SerializedName

class ParticipantsRequest(
    @SerializedName("participantId")
    val participantId: Int,

    @SerializedName("amount")
    val amount: Double,
)