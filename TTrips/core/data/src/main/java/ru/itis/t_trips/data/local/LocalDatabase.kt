package ru.itis.t_trips.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.t_trips.data.local.dao.ExpenseReceiptUrlDao
import ru.itis.t_trips.data.local.dao.TripPictureUrlDao
import ru.itis.t_trips.data.local.entity.ExpenseReceiptUrlEntity
import ru.itis.t_trips.data.local.entity.TripPictureUrlEntity

@Database(
    entities = [
        TripPictureUrlEntity::class,
        ExpenseReceiptUrlEntity::class
    ],
    version = 1
)
internal abstract class LocalDatabase : RoomDatabase() {
    abstract val tripPictureUrlDao: TripPictureUrlDao
    abstract val expenseReceiptUrlDao: ExpenseReceiptUrlDao
}