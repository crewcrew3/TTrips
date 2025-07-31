package ru.itis.t_trips.authentication

data class AuthFormState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)