package ru.itis.t_trips.network.model.request.auth

import com.google.gson.annotations.SerializedName

class RegisterUserRequest(

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("repeatPassword")
    val repeatPassword: String,
)