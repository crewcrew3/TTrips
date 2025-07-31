package ru.itis.t_trips.notifications

import ru.itis.t_trips.domain.model.notification.NotificationModel

internal sealed interface NotificationsScreenEvent {
    data object OnScreenInit : NotificationsScreenEvent
    data class onNotificationClick(val notification: NotificationModel) : NotificationsScreenEvent
}