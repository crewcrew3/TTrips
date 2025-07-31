package ru.itis.t_trips.addactualexpense.addexpense

import android.content.Context
import android.net.Uri
import ru.itis.t_trips.ui.model.Contact

internal sealed interface AddActualExpenseScreenEvent {
    data class OnScreenInit(val tripId: Int) : AddActualExpenseScreenEvent
    data object OnBackBtnClick : AddActualExpenseScreenEvent
    data class OnSaveBtnClick(
        val tripId: Int,
        val title: String,
        val category: String,
        val participantsStr: String,
        val uri: Uri?,
        val context: Context?
    ) : AddActualExpenseScreenEvent
    data class OnDivideBtnClick(val participants: List<Contact>, val tripId: Int, val totalAmount: Double) : AddActualExpenseScreenEvent
    data class OnFormFieldChanged(
        val description: String? = null,
        val amount: String? = null,
        val category: String? = null,
        val imageUri: Uri? = null,
        val participants: List<Contact>? = null
    ) : AddActualExpenseScreenEvent
}