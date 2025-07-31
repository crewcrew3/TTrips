package ru.itis.t_trips.network.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.itis.t_trips.network.model.request.trip.AddTripRequest
import ru.itis.t_trips.network.model.request.user.PhoneNumberRequest
import ru.itis.t_trips.network.model.response.trip.DebtResponse
import ru.itis.t_trips.network.model.response.trip.TripMemberResponse
import ru.itis.t_trips.network.model.response.trip.TripResponse

interface TripApi {

    @GET("trips")
    suspend fun getTripList(
        @Query("onlyCreator") onlyCreator: Boolean,
        @Query("onlyArchive") onlyArchive: Boolean,
    ): List<TripResponse?>

    @POST("trips")
    suspend fun addTrip(@Body request: AddTripRequest): TripResponse?

    @POST("trips/{tripId}/members")
    suspend fun inviteMemberForTrip(@Path("tripId") tripId: Int, @Body request: PhoneNumberRequest)

    @GET("trips/{id}")
    suspend fun getTripInfo(@Path("id") tripId: Int): TripResponse?

    @PATCH("trips/{id}/archive")
    suspend fun finishTrip(@Path("id") tripId: Int)

    @GET("trips/{tripId}/members")
    suspend fun getTripMembers(@Path("tripId") tripId: Int): List<TripMemberResponse?>

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") tripId: Int)

    @GET("debts/{tripId}")
    suspend fun getTripDebts(@Path("tripId") tripId: Int): List<DebtResponse?>
}