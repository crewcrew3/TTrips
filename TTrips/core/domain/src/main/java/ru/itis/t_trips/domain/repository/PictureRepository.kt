package ru.itis.t_trips.domain.repository

interface PictureRepository {
    suspend fun getUserPhotoUrl(): String?
    suspend fun saveUserPhotoUrl(url: String)
    suspend fun getTripPictureUrlById(tripId: Int): String?
    suspend fun saveTripPictureUrl(url: String, tripId: Int)
    suspend fun saveExpenseReceiptPictureUrl(url: String, expenseId: Int)
    suspend fun getExpenseReceiptPictureUrlById(expenseId: Int): String?
}