package ru.itis.t_trips.network.model.request.user

import com.google.gson.annotations.SerializedName

class UpdateUserCredentialsRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("password")
    val password: String,
)