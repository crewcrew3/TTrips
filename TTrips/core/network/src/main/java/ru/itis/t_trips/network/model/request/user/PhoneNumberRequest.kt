package ru.itis.t_trips.network.model.request.user

import com.google.gson.annotations.SerializedName

class PhoneNumberRequest(
    @SerializedName("phone")
    val phoneNumber: String,
)