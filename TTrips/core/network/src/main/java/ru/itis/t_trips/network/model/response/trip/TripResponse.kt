package ru.itis.t_trips.network.model.response.trip

import com.google.gson.annotations.SerializedName

class TripResponse(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("startDate")
    val startDate: String?,

    @SerializedName("endDate")
    val endDate: String?,

    @SerializedName("totalBudget")
    val budget: Double?
)