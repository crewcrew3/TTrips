package ru.itis.t_trips.data.remote.mapper

import ru.itis.t_trips.domain.model.UserProfileModel
import ru.itis.t_trips.network.model.response.user.UserProfileResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class UserProfileResponseMapper @Inject constructor() {
    fun map(input: UserProfileResponse?): UserProfileModel {
        val response = requireNotNull(input) { ExceptionCode.PROFILE_RESPONSE }
        return UserProfileModel(
            id = requireNotNull(response.id) { ExceptionCode.PROFILE_RESPONSE },
            firstName = requireNotNull(response.firstName) { ExceptionCode.PROFILE_RESPONSE },
            lastName = requireNotNull(response.lastName) { ExceptionCode.PROFILE_RESPONSE },
            phoneNumber = requireNotNull(response.phoneNumber) { ExceptionCode.PROFILE_RESPONSE },
        )
    }
}
