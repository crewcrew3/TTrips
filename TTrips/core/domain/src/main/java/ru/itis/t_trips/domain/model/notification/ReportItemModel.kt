package ru.itis.t_trips.domain.model.notification

class ReportItemModel(
    id: Int,
    tripId: Int,
) : NotificationModel(
    id = id,
    tripId = tripId,
)