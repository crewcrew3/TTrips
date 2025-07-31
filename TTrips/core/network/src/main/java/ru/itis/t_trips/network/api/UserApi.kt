package ru.itis.t_trips.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.itis.t_trips.network.model.request.user.UpdateUserCredentialsRequest
import ru.itis.t_trips.network.model.request.user.UpdateUserInfoRequest
import ru.itis.t_trips.network.model.response.user.UpdateUserCredentialsResponse
import ru.itis.t_trips.network.model.response.user.UserProfileResponse

interface UserApi {

    @GET("users/me")
    suspend fun getUserProfile(): UserProfileResponse?

    @PATCH("users/edit")
    suspend fun updateUserProfile(@Body request: UpdateUserInfoRequest): UserProfileResponse?

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): UserProfileResponse?

    //!!!!!!!!!
    @PUT("")
    suspend fun updateUserCredentials(@Body request: UpdateUserCredentialsRequest): UpdateUserCredentialsResponse?

    @GET
    suspend fun getUsersByPhoneNumbers(@Body phoneNumbers: List<String>): List<UserProfileResponse?>
}