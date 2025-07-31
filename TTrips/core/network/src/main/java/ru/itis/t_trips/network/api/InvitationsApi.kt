package ru.itis.t_trips.network.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.itis.t_trips.network.model.response.invitation.InvitationResponse

interface InvitationsApi {

    @GET("invitations")
    suspend fun getUsersInvitations() : List<InvitationResponse?>

    @POST("invitations/{invitationId}/accept")
    suspend fun acceptInvitation(@Path("invitationId") invitationId: Int)

    @POST("invitations/{invitationId}/reject")
    suspend fun rejectInvitation(@Path("invitationId") invitationId: Int)
}