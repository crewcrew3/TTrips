package ru.itis.t_trips.network.model.response.invitation

import com.google.gson.annotations.SerializedName

class InvitationResponse (
    @SerializedName("id")
    val id: Int?,

    @SerializedName("tripId")
    val tripId: Int?,

    @SerializedName("invitedUserId")
    val invitedUserId: Int?,

    @SerializedName("inviterId")
    val inviterId: Int?,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("status")
    val status: String?,
)