package ru.itis.t_trips.addtrip.commoninfo

import ru.itis.t_trips.ui.model.Contact

internal sealed interface AddTripScreenState {
    data object Initial : AddTripScreenState
    data class Result(val contacts: List<Contact>) : AddTripScreenState
}