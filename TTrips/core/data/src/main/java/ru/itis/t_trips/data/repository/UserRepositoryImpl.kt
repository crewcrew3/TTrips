package ru.itis.t_trips.data.repository

import retrofit2.HttpException
import ru.itis.t_trips.data.remote.mapper.UpdateUserCredentialsResponseMapper
import ru.itis.t_trips.data.remote.mapper.UserProfileResponseMapper
import ru.itis.t_trips.domain.model.UpdateUserCredentialsModel
import ru.itis.t_trips.domain.model.UserProfileModel
import ru.itis.t_trips.domain.repository.UserRepository
import ru.itis.t_trips.network.api.UserApi
import ru.itis.t_trips.network.model.request.user.UpdateUserCredentialsRequest
import ru.itis.t_trips.network.model.request.user.UpdateUserInfoRequest
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.runCatchingNonCancellable
import java.net.HttpURLConnection
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userProfileResponseMapper: UserProfileResponseMapper,
    private val updateUserCredentialsResponseMapper: UpdateUserCredentialsResponseMapper
) : UserRepository {

    override suspend fun getUserProfile(): UserProfileModel {
        return runCatchingNonCancellable {
            userApi.getUserProfile().let(userProfileResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun updateUserProfile(firstName: String, lastName: String): UserProfileModel {
        return runCatchingNonCancellable {
            userApi.updateUserProfile(
                UpdateUserInfoRequest(
                    firstName = firstName,
                    lastName = lastName
                )
            ).let(userProfileResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw IllegalArgumentException(ExceptionCode.INVALID_DATA)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun getUserById(userId: Int): UserProfileModel {
        return runCatchingNonCancellable {
            userApi.getUserById(userId).let(userProfileResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }


    //!!!!!!!!!!!!!!!!!!!
    override suspend fun updateUserCredentials(phoneNumber: String, password: String): UpdateUserCredentialsModel {
        return runCatchingNonCancellable {
            userApi.updateUserCredentials(
                UpdateUserCredentialsRequest(
                    phoneNumber = phoneNumber,
                    password = password,
                )
            ) .let(updateUserCredentialsResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> throw IllegalArgumentException(ExceptionCode.INVALID_DATA)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun getUsersByPhoneNumbers(phoneNumbers: List<String>): List<UserProfileModel> {
        return runCatchingNonCancellable {
            userApi.getUsersByPhoneNumbers(phoneNumbers).map(userProfileResponseMapper::map)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    when (exception.code()) {
                        HttpURLConnection.HTTP_NOT_FOUND -> throw IllegalArgumentException(ExceptionCode.NOT_FOUND)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> throw IllegalArgumentException(ExceptionCode.UNAUTHORIZED)
                        HttpURLConnection.HTTP_FORBIDDEN -> throw IllegalArgumentException(ExceptionCode.FORBIDDEN)
                    }
                }
            }

        }.getOrThrow()
    }
}