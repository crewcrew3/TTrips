package ru.itis.t_trips.domain.model

class DebtModel(
    val tripId: Int,
    val amount: Double,
    val debtor: UserProfileModel,
    val creditor: UserProfileModel,
)