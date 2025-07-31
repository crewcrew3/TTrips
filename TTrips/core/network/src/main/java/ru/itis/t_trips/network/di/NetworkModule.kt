package ru.itis.t_trips.network.di

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.t_trips.network.api.AuthApi
import ru.itis.t_trips.network.api.ExpenseApi
import ru.itis.t_trips.network.api.InvitationsApi
import ru.itis.t_trips.network.api.TripApi
import ru.itis.t_trips.network.api.UserApi
import ru.itis.t_trips.network.interceptor.AccessTokenInterceptor
import ru.itis.t_trips.network.tokenlogic.TokenAuthenticator
import ru.itis.t_trips.network.tokenlogic.TokenStorage
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import ru.itis.t_trips.network.BuildConfig as networkConfig

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    fun provideTokenAuthenticator(
        tokenStorage: TokenStorage
    ): TokenAuthenticator {
        val tokenAuthenticator = TokenAuthenticator(tokenStorage)
        return tokenAuthenticator
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): AuthApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder = Retrofit.Builder()
            .baseUrl(networkConfig.TRAVEL_TBANK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTripApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): TripApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder = Retrofit.Builder()
            .baseUrl(networkConfig.TRAVEL_TBANK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(TripApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): UserApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder = Retrofit.Builder()
            .baseUrl(networkConfig.TRAVEL_TBANK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExpenseApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): ExpenseApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder = Retrofit.Builder()
            .baseUrl(networkConfig.TRAVEL_TBANK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(ExpenseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInvitationsApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): InvitationsApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder = Retrofit.Builder()
            .baseUrl(networkConfig.TRAVEL_TBANK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(InvitationsApi::class.java)
    }


    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        accessTokenInterceptor: AccessTokenInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {

        val cacheSize = 5L * 1024 * 1024 // 5 MB
        val cache = Cache(context.cacheDir, cacheSize)

        val builder = if (networkConfig.DEBUG) {
            getUnsafeOkHttpClientBuilder()
        } else {
            OkHttpClient.Builder()
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context)) {
                // Если есть сеть, кэшируем максимум 5 секунд
                request.newBuilder()
                    .header("Cache-Control", "public, max-age=5")
                    .build()
            } else {
                // Если сети нет, используем кэш до 7 дней
                request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=604800")
                    .build()
            }
            chain.proceed(request)
        }

        builder.cache(cache)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)

        return builder.build()
    }

    private fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create()
    }

    @SuppressLint("CustomX509TrustManager")
    private fun getUnsafeOkHttpClientBuilder(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }
}