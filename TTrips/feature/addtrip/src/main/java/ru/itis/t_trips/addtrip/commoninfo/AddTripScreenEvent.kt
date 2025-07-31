package ru.itis.t_trips.addtrip.commoninfo

import android.net.Uri
import ru.itis.t_trips.ui.model.Contact

internal sealed interface AddTripScreenEvent {
    data object OnBackBtnClick: AddTripScreenEvent
    data object OnAddMembersBtnClick: AddTripScreenEvent
    data class OnSaveBtnClick(
        val title: String,
        val startDate: String,
        val endDate: String,
        val budget: String,
        val uri: Uri?,
        val membersList: List<Contact>,
    ) : AddTripScreenEvent
    data class OnAddMembers(val contacts: String) : AddTripScreenEvent
    data class OnRemoveContact(val contact: Contact) : AddTripScreenEvent
    data class OnFormFieldChanged(
        val title: String? = null,
        val startDate: String? = null,
        val endDate: String? = null,
        val budget: String? = null,
        val startDateUi: String? = null,
        val endDateUi: String? = null,
        val uri: Uri? = null,
    ) : AddTripScreenEvent
}