package ru.itis.t_trips.notifications

import ru.itis.t_trips.domain.model.notification.NotificationModel

internal sealed interface NotificationsScreenState {
    data object Initial : NotificationsScreenState
    data object Loading : NotificationsScreenState
    data class NotificationsResult(val notifications: List<NotificationModel>) : NotificationsScreenState
}