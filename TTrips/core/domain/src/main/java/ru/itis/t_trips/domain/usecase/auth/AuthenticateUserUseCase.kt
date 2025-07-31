package ru.itis.t_trips.domain.usecase.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(phoneNumber: String, password: String) {
        withContext(dispatcher) {
            authRepository.logIn(phoneNumber, password)
        }
    }
}