package ru.itis.t_trips.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trip_pictures_urls",
    indices = [Index(
        value = ["trip_id"],
        unique = true
    )]
)
internal data class TripPictureUrlEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trip_pic_id")
    var id: Int = 0,

    @ColumnInfo(name = "trip_id")
    var tripId: Int,

    @ColumnInfo(name = "trip_pic_url")
    var url: String,
)