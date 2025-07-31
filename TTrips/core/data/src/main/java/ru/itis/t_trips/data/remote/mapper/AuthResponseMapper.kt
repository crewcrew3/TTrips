package ru.itis.t_trips.data.remote.mapper

import ru.itis.t_trips.domain.model.AuthModel
import ru.itis.t_trips.network.model.response.auth.AuthResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class AuthResponseMapper @Inject constructor() {
    fun map(input: AuthResponse?): AuthModel {
        val response = requireNotNull(input) { ExceptionCode.AUTH_TOKEN_RESPONSE }
        return AuthModel(
            accessToken = requireNotNull(response.accessToken) { ExceptionCode.AUTH_TOKEN_RESPONSE },
            refreshToken = requireNotNull(response.refreshToken) { ExceptionCode.AUTH_TOKEN_RESPONSE },
        )
    }
}