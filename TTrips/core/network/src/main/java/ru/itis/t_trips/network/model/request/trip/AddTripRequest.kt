package ru.itis.t_trips.network.model.request.trip

import com.google.gson.annotations.SerializedName

class AddTripRequest(

    @SerializedName("title")
    val title: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("totalBudget")
    val budget: Double
)