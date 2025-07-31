package ru.itis.t_trips.addtrip.contacts

import ru.itis.t_trips.ui.model.Contact

internal sealed interface ContactsScreenState {
    data object Initial : ContactsScreenState
    data class Result(val contacts: List<Contact>) : ContactsScreenState
}