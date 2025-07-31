package ru.itis.t_trips.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses_receipt_pictures_urls",
    indices = [Index(
        value = ["expense_id"],
        unique = true
    )]
)
internal data class ExpenseReceiptUrlEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_pic_id")
    var id: Int = 0,

    @ColumnInfo(name = "expense_id")
    var expenseId: Int,

    @ColumnInfo(name = "expense_pic_url")
    var url: String,
)