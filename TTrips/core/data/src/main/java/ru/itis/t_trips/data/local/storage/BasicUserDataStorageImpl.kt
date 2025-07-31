package ru.itis.t_trips.data.local.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage
import ru.itis.t_trips.utils.PrefProperties
import javax.inject.Inject

internal class BasicUserDataStorageImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : BasicUserDataStorage {
    private companion object PreferencesKeys {
        val USER_PHONE_NUMBER = stringPreferencesKey(PrefProperties.USER_PHONE_NUMBER)
        val USER_PHOTO_URL = stringPreferencesKey(PrefProperties.USER_PHOTO_URL)
        val APP_LOCALE = stringPreferencesKey(PrefProperties.APP_LOCALE)
    }

    override suspend fun saveUserPhoneNumber(phoneNumber: String) {
        dataStore.edit { prefs ->
            prefs[USER_PHONE_NUMBER] = phoneNumber
        }
    }

    override suspend fun getUserPhoneNumber(): String? {
        val prefs = dataStore.data.first()
        return prefs[USER_PHONE_NUMBER]
    }

    override suspend fun saveUserPhotoUrl(url: String) {
        dataStore.edit { prefs ->
            prefs[USER_PHOTO_URL] = url
        }
    }

    override suspend fun getUserPhotoUrl(): String? {
        val prefs = dataStore.data.first()
        return prefs[USER_PHOTO_URL]
    }

    override suspend fun clearUserDataOnLogOut() { //photo url не удаляю при выходе
        dataStore.edit { prefs ->
            prefs.remove(USER_PHONE_NUMBER)
        }
    }

    override suspend fun saveAppLocale(languageCode: String) {
        dataStore.edit { prefs ->
            prefs[APP_LOCALE] = languageCode
        }
    }

    override suspend fun getAppLocale(): String? {
        val prefs = dataStore.data.first()
        return prefs[APP_LOCALE]
    }
}