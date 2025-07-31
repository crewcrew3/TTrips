package ru.itis.t_trips.data.remote.mapper

import ru.itis.t_trips.domain.model.DebtModel
import ru.itis.t_trips.network.model.response.user.UserProfileResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class DebtResponseMapper @Inject constructor(
    private val userProfileResponseMapper: UserProfileResponseMapper,
) {
    fun map(
        tripId: Int?,
        amount: Double?,
        inputCreditor: UserProfileResponse?,
        inputDebtor: UserProfileResponse?
    ): DebtModel {
        return DebtModel(
            tripId = requireNotNull(tripId) { ExceptionCode.DEBT_RESPONSE },
            amount = requireNotNull(amount) { ExceptionCode.DEBT_RESPONSE },
            creditor = userProfileResponseMapper.map(inputCreditor),
            debtor = userProfileResponseMapper.map(inputDebtor)
        )
    }
}