package ru.itis.t_trips.notifications

import androidx.annotation.StringRes

internal sealed interface NotificationsScreenEffect {
    data class Message(@StringRes val message: Int) : NotificationsScreenEffect
}