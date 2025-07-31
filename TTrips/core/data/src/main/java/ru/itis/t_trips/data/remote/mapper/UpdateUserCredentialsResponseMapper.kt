package ru.itis.t_trips.data.remote.mapper

import ru.itis.t_trips.domain.model.UpdateUserCredentialsModel
import ru.itis.t_trips.network.model.response.user.UpdateUserCredentialsResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class UpdateUserCredentialsResponseMapper @Inject constructor() {
    fun map(input: UpdateUserCredentialsResponse?): UpdateUserCredentialsModel {
        val response = requireNotNull(input) { ExceptionCode.UPDATE_USER_CREDENTIALS }
        return UpdateUserCredentialsModel(
            phoneNumber = requireNotNull(response.phoneNumber) { ExceptionCode.UPDATE_USER_CREDENTIALS },
            password = requireNotNull(response.password) { ExceptionCode.UPDATE_USER_CREDENTIALS },
        )
    }
}