package ru.itis.t_trips.network.model.request.auth

import com.google.gson.annotations.SerializedName

class AuthenticateUserRequest(

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("password")
    val password: String,
)