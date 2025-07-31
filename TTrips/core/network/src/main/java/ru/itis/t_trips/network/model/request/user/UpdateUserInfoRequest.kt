package ru.itis.t_trips.network.model.request.user

import com.google.gson.annotations.SerializedName

class UpdateUserInfoRequest (
    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,
)