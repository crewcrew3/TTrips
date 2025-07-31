package ru.itis.t_trips.actualexpenseinfo

import android.content.Context
import android.net.Uri

internal sealed interface ActualExpenseInfoScreenEvent {
    data class OnScreenInit(val expenseId: Int) : ActualExpenseInfoScreenEvent
    data class OnReceiptAdded(val receipt: Uri?, val expenseId: Int, val context: Context?) : ActualExpenseInfoScreenEvent
    data class OnBackBtnClick(val tripId: Int) : ActualExpenseInfoScreenEvent
    data class OnDeleteExpenseIconClick(val expenseId: Int, val tripId: Int) : ActualExpenseInfoScreenEvent
}