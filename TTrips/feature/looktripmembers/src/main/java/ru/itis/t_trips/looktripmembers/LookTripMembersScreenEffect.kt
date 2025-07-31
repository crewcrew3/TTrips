package ru.itis.t_trips.looktripmembers

import androidx.annotation.StringRes

internal sealed interface LookTripMembersScreenEffect {
    data class Message(@StringRes val message: Int) : LookTripMembersScreenEffect
}