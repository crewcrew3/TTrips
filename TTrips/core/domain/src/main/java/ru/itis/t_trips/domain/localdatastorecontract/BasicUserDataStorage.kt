package ru.itis.t_trips.domain.localdatastorecontract

interface BasicUserDataStorage {
    suspend fun saveUserPhoneNumber(phoneNumber: String)
    suspend fun getUserPhoneNumber(): String?
    suspend fun saveUserPhotoUrl(url: String)
    suspend fun getUserPhotoUrl(): String?
    suspend fun clearUserDataOnLogOut()
    suspend fun saveAppLocale(languageCode: String)
    suspend fun getAppLocale(): String?
}