package ru.itis.t_trips.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.itis.t_trips.network.model.request.auth.AuthenticateUserRequest
import ru.itis.t_trips.network.model.request.auth.RegisterUserRequest
import ru.itis.t_trips.network.model.response.auth.AuthResponse
import ru.itis.t_trips.network.model.response.auth.LogOutResponse
import ru.itis.t_trips.network.model.response.auth.UserExistenceResponse
import ru.itis.t_trips.utils.NetworkProperties

interface AuthApi {

    @GET("check")
    suspend fun isUserExist(
        @Query("phone") phoneNumber: String,
    ) : UserExistenceResponse?

    @POST("login")
    suspend fun authenticateUser(@Body request: AuthenticateUserRequest): AuthResponse?

    @POST("register")
    suspend fun registerUser(@Body request: RegisterUserRequest): AuthResponse?

    @POST("refresh")
    suspend fun refreshToken(@Header(NetworkProperties.REFRESH_TOKEN_HEADER) refreshToken: String): AuthResponse?

    @POST("logout")
    suspend fun logOutUser(): LogOutResponse?
}