package ru.itis.t_trips.data.repository

import ru.itis.t_trips.data.local.dao.ExpenseReceiptUrlDao
import ru.itis.t_trips.data.local.dao.TripPictureUrlDao
import ru.itis.t_trips.data.local.entity.ExpenseReceiptUrlEntity
import ru.itis.t_trips.data.local.entity.TripPictureUrlEntity
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage
import ru.itis.t_trips.domain.repository.PictureRepository
import javax.inject.Inject

//репо чтобы работать с локальными картинками (костыльненько, но на бэке картинок нет)
internal class PictureRepositoryImpl @Inject constructor(
    private val basicUserDataStorage: BasicUserDataStorage,
    private val tripPictureUrlDao: TripPictureUrlDao,
    private val expenseReceiptUrlDao: ExpenseReceiptUrlDao,
) : PictureRepository {

    override suspend fun getUserPhotoUrl(): String? {
        return basicUserDataStorage.getUserPhotoUrl()
    }

    override suspend fun saveUserPhotoUrl(url: String) {
        basicUserDataStorage.saveUserPhotoUrl(url)
    }

    override suspend fun getTripPictureUrlById(tripId: Int): String? {
        return tripPictureUrlDao.getTripPicUrlByTripId(tripId)?.url
    }

    override suspend fun saveTripPictureUrl(url: String, tripId: Int) {
        tripPictureUrlDao.saveTripPicUrl(
            TripPictureUrlEntity(
                tripId = tripId,
                url = url,
            )
        )
    }

    override suspend fun saveExpenseReceiptPictureUrl(url: String, expenseId: Int) {
        expenseReceiptUrlDao.saveExpenseReceiptPicUrl(
            ExpenseReceiptUrlEntity(
                expenseId = expenseId,
                url = url,
            )
        )
    }

    override suspend fun getExpenseReceiptPictureUrlById(expenseId: Int): String? {
        return expenseReceiptUrlDao.getExpenseReceiptPicUrlByExpenseId(expenseId)?.url
    }
}