package ru.itis.t_trips.data.repository

import retrofit2.HttpException
import ru.itis.t_trips.data.remote.mapper.AuthResponseMapper
import ru.itis.t_trips.domain.exception.AuthenticationException
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage
import ru.itis.t_trips.domain.repository.AuthRepository
import ru.itis.t_trips.network.api.AuthApi
import ru.itis.t_trips.network.model.request.auth.AuthenticateUserRequest
import ru.itis.t_trips.network.model.request.auth.RegisterUserRequest
import ru.itis.t_trips.network.tokenlogic.TokenStorage
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.runCatchingNonCancellable
import java.net.HttpURLConnection
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authResponseMapper: AuthResponseMapper,
    private val tokenStorage: TokenStorage,
    private val basicUserDataStorage: BasicUserDataStorage,
) : AuthRepository {

    override suspend fun isUserExist(phoneNumber: String): Boolean {
        return runCatchingNonCancellable {
            authApi.isUserExist(phoneNumber)?.exists ?: throw IllegalArgumentException(ExceptionCode.USER_EXISTS_RESPONSE)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    if (exception.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        throw IllegalArgumentException(ExceptionCode.INVALID_DATA)
                    }
                }
            }
        }.getOrThrow()
    }

    override suspend fun logIn(phoneNumber: String, password: String) {
        runCatchingNonCancellable {
            authApi.authenticateUser(
                AuthenticateUserRequest(
                    phoneNumber = phoneNumber,
                    password = password,
                )
            ).let(authResponseMapper::map)
        }.onSuccess { authModel ->
            tokenStorage.saveAccessToken(authModel.accessToken)
            tokenStorage.saveRefreshToken(authModel.refreshToken)
            basicUserDataStorage.saveUserPhoneNumber(phoneNumber)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    if (exception.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        throw AuthenticationException(ExceptionCode.WRONG_CREDENTIALS)
                    }
                }
            }
        }.getOrThrow() //возвращаем результат, либо пробрасываем ошибки выше
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String,
        repeatPassword: String
    ) {
        runCatchingNonCancellable {
            authApi.registerUser(
                RegisterUserRequest(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    password = password,
                    repeatPassword = repeatPassword
                )
            ).let(authResponseMapper::map)
        }.onSuccess { authModel ->
            tokenStorage.saveAccessToken(authModel.accessToken)
            tokenStorage.saveRefreshToken(authModel.refreshToken)
            basicUserDataStorage.saveUserPhoneNumber(phoneNumber)
        }.onFailure { exception ->
            when (exception) {
                is HttpException -> {
                    if (exception.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        throw AuthenticationException(ExceptionCode.INVALID_DATA)
                    }
                }
            }
        }.getOrThrow()
    }

    //!!!!!!!!!!!!!!!!!
    override suspend fun logOut(): Boolean {
        tokenStorage.clearTokens()
        basicUserDataStorage.clearUserDataOnLogOut()
        return true
//        return runCatchingNonCancellable {
//            authApi.logOutUser()?.success ?: throw IllegalArgumentException(ExceptionCode.LOG_OUT_RESPONSE)
//        }.onSuccess {
//            tokenStorage.clearTokens()
//            basicUserDataStorage.clearUserDataOnLogOut()
//        }.onFailure { exception ->
//            when (exception) {
//                is HttpException -> {
//                    if (exception.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                        throw AuthenticationException(ExceptionCode.UNAUTHORIZED)
//                    }
//                }
//            }
//        }.getOrThrow()
    }
}