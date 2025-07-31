package ru.itis.t_trips.domain.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.UpdateUserCredentialsModel
import ru.itis.t_trips.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserCredentialsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(phoneNumber: String, password: String): UpdateUserCredentialsModel {
        return withContext(dispatcher) {
            userRepository.updateUserCredentials(phoneNumber = phoneNumber, password = password)
        }
    }
}