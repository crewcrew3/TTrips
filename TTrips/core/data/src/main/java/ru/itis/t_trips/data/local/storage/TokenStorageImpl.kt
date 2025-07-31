package ru.itis.t_trips.data.local.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import ru.itis.t_trips.network.tokenlogic.TokenStorage
import ru.itis.t_trips.utils.PrefProperties
import javax.inject.Inject

internal class TokenStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : TokenStorage {

    private companion object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey(PrefProperties.ACCESS_TOKEN)
        val REFRESH_TOKEN = stringPreferencesKey(PrefProperties.REFRESH_TOKEN)
    }

    override suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
        }
    }

    override suspend fun getAccessToken(): String? {
        val prefs = dataStore.data.first()
        return prefs[ACCESS_TOKEN]
    }

    override suspend fun saveRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = token
        }
    }

    override suspend fun getRefreshToken(): String? {
        val prefs = dataStore.data.first()
        return prefs[REFRESH_TOKEN]
    }

    override suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
        }
    }
}