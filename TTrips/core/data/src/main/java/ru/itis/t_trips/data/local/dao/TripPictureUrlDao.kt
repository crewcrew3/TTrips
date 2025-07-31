package ru.itis.t_trips.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.t_trips.data.local.entity.TripPictureUrlEntity

@Dao
internal interface TripPictureUrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTripPicUrl(pic: TripPictureUrlEntity)

    @Query("SELECT * FROM trip_pictures_urls WHERE trip_id = :tripId")
    suspend fun getTripPicUrlByTripId(tripId: Int) : TripPictureUrlEntity?
}