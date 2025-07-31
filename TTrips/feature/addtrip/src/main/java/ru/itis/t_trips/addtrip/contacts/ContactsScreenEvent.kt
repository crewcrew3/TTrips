package ru.itis.t_trips.addtrip.contacts

import ru.itis.t_trips.ui.model.Contact

internal sealed interface ContactsScreenEvent {
    data object OnBackBtnClick : ContactsScreenEvent
    data object OnScreenInit : ContactsScreenEvent
    data class OnNextBtnClick(val contacts: List<Contact>) : ContactsScreenEvent
}