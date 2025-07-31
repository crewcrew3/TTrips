package ru.itis.t_trips.network.tokenlogic

interface TokenStorage {

    suspend fun saveAccessToken(token: String)

    suspend fun getAccessToken(): String?

    suspend fun saveRefreshToken(token: String)

    suspend fun getRefreshToken(): String?

    suspend fun clearTokens()
}