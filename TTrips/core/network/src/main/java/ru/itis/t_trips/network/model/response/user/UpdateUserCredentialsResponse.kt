package ru.itis.t_trips.network.model.response.user

import com.google.gson.annotations.SerializedName

class UpdateUserCredentialsResponse(
    @SerializedName("phoneNumber")
    val phoneNumber: String?,

    @SerializedName("password")
    val password: String?,
)