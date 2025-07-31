package ru.itis.t_trips.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.t_trips.data.local.entity.ExpenseReceiptUrlEntity

@Dao
internal interface ExpenseReceiptUrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExpenseReceiptPicUrl(pic: ExpenseReceiptUrlEntity)

    @Query("SELECT * FROM expenses_receipt_pictures_urls WHERE expense_id = :expenseId")
    suspend fun getExpenseReceiptPicUrlByExpenseId(expenseId: Int) : ExpenseReceiptUrlEntity?
}