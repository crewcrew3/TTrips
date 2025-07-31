package ru.itis.t_trips.network.model.response.trip

import com.google.gson.annotations.SerializedName

class TripMemberResponse(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("tripId")
    val tripId: Int?,

    @SerializedName("userId")
    val userId: Int?,
)