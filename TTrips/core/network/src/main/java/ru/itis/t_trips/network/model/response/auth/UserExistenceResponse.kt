package ru.itis.t_trips.network.model.response.auth

import com.google.gson.annotations.SerializedName

class UserExistenceResponse(
    @SerializedName("exists")
    val exists: Boolean?
)