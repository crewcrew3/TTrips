package ru.itis.t_trips.data.repository

import retrofit2.HttpException
import ru.itis.t_trips.data.remote.mapper.notification.InvitationItemResponseMapper
import ru.itis.t_trips.data.remote.mapper.InvitationResponseMapper
import ru.itis.t_trips.domain.model.InvitationModel
import ru.itis.t_trips.domain.model.notification.InvitationItemModel
import ru.itis.t_trips.domain.repository.InvitationsRepository
import ru.itis.t_trips.network.api.InvitationsApi
import ru.itis.t_trips.network.api.TripApi
import ru.itis.t_trips.network.api.UserApi
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.runCatchingNonCancellable
import java.net.HttpURLConnection
import javax.inject.Inject

internal class InvitationsRepositoryImpl @Inject constructor(
    private val invitationsApi: InvitationsApi,
    private val tripApi: TripApi,
    private val userApi: UserApi,
    private val invitationResponseMapper: InvitationResponseMapper,
    private val invitationItemResponseMapper: InvitationItemResponseMapper,
) : InvitationsRepository {

    override suspend fun getUsersInvitations(): List<InvitationItemModel> {
        return runCatchingNonCancellable {
            invitationsApi.getUsersInvitations().map(invitationItemResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun getInvitationInfo(invitationId: Int, userId: Int, tripId: Int): InvitationModel {
        return runCatchingNonCancellable {
            invitationResponseMapper.map(
                invitationId = invitationId,
                inputUser = userApi.getUserById(userId),
                inputTrip = tripApi.getTripInfo(tripId),
            )
        }.onFailure {
            throw IllegalArgumentException(ExceptionCode.INVITATION_DETAILS_RESPONSE)
        }.getOrThrow()
    }

    override suspend fun acceptInvitation(invitationId: Int) {
        runCatchingNonCancellable {
            invitationsApi.acceptInvitation(invitationId = invitationId)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                    }
                }
            }
        }
    }

    override suspend fun rejectInvitation(invitationId: Int) {
        runCatchingNonCancellable {
            invitationsApi.rejectInvitation(invitationId = invitationId)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                    }
                }
            }
        }
    }
}