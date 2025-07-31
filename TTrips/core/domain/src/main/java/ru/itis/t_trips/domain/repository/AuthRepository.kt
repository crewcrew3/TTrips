package ru.itis.t_trips.domain.repository

interface AuthRepository {
    suspend fun isUserExist(phoneNumber: String): Boolean
    suspend fun logIn(phoneNumber: String, password: String)
    suspend fun signUp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String,
        repeatPassword: String
    )
    suspend fun logOut(): Boolean
}