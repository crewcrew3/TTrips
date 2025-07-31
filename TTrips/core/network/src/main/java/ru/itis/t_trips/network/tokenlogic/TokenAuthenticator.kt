package ru.itis.t_trips.network.tokenlogic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.itis.t_trips.network.api.AuthApi
import ru.itis.t_trips.utils.NetworkProperties
import javax.inject.Inject

internal class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage,
) : Authenticator {

    @set:Inject
    lateinit var authApi: AuthApi

    override fun authenticate(route: Route?, response: Response): Request? {

        val tokens = runBlocking(Dispatchers.IO) {
            runCatching {
                authApi.refreshToken(tokenStorage.getRefreshToken() ?: "")
            }.getOrNull()
        }

        return tokens?.let { safeTokens ->
            runBlocking(Dispatchers.IO) {
                if (safeTokens.accessToken != null && safeTokens.refreshToken != null) {
                    tokenStorage.saveAccessToken(safeTokens.accessToken)
                    tokenStorage.saveRefreshToken(safeTokens.refreshToken)
                }
            }
            response.request.newBuilder()
                .header(NetworkProperties.AUTH_HEADER, "Bearer ${safeTokens.accessToken}")
                .build()
        }
    }
}