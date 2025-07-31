package ru.itis.t_trips.network.model.response.user

import com.google.gson.annotations.SerializedName

class UserProfileResponse(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("phoneNumber")
    val phoneNumber: String?,

    @SerializedName("role")
    val role: String?,
)