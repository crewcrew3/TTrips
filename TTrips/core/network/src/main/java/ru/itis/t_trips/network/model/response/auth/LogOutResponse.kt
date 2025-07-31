package ru.itis.t_trips.network.model.response.auth

import com.google.gson.annotations.SerializedName

class LogOutResponse(
    @SerializedName("success")
    val success: Boolean?
)