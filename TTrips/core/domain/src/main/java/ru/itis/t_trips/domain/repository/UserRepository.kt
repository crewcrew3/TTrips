package ru.itis.t_trips.domain.repository

import ru.itis.t_trips.domain.model.UpdateUserCredentialsModel
import ru.itis.t_trips.domain.model.UserProfileModel

interface UserRepository {
    suspend fun getUserProfile(): UserProfileModel
    suspend fun getUsersByPhoneNumbers(phoneNumbers: List<String>): List<UserProfileModel>
    suspend fun updateUserProfile(firstName: String, lastName: String): UserProfileModel
    suspend fun getUserById(userId: Int): UserProfileModel
    suspend fun updateUserCredentials(phoneNumber: String, password: String): UpdateUserCredentialsModel
}