package ru.itis.t_trips.addtrip.contacts

import androidx.annotation.StringRes

internal sealed interface ContactsScreenEffect {
    data class Message(@StringRes val message: Int) : ContactsScreenEffect
}