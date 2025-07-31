package ru.itis.t_trips.looktripmembers

import ru.itis.t_trips.ui.model.Contact

internal sealed interface LookTripMembersScreenState {
    data object Initial : LookTripMembersScreenState
    data object Loading : LookTripMembersScreenState
    data class MembersResult(val members: List<Contact>) : LookTripMembersScreenState
}