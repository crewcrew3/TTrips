package ru.itis.t_trips.network.interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.itis.t_trips.network.tokenlogic.TokenStorage
import ru.itis.t_trips.utils.NetworkProperties
import javax.inject.Inject

internal class AccessTokenInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking(Dispatchers.IO) {
            tokenStorage.getAccessToken()
        }
        val request = chain.request().newBuilder()
            .addHeader(NetworkProperties.AUTH_HEADER, "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}