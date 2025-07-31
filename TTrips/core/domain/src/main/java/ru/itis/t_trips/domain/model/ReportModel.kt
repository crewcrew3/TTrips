package ru.itis.t_trips.domain.model

class ReportModel(
    val trip: TripModel,
    val debtsMap: Map<UserProfileModel, Double> //второе число - сумма долга перед текущим пользователем со стороны остальных участников
)