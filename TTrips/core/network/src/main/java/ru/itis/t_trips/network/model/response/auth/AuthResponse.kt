package ru.itis.t_trips.network.model.response.auth

import com.google.gson.annotations.SerializedName

class AuthResponse(

    @SerializedName("accessToken")
    val accessToken: String?,

    @SerializedName("refreshToken")
    val refreshToken: String?,
)