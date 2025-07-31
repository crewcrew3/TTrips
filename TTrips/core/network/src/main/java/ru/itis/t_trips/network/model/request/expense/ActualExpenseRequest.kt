package ru.itis.t_trips.network.model.request.expense

import com.google.gson.annotations.SerializedName

class ActualExpenseRequest(

    @SerializedName("description")
    val title: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("participants")
    val participants: List<ParticipantsRequest>,
)