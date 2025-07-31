package ru.itis.t_trips.domain.usecase.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String,
        repeatPassword: String
    ) {
        withContext(dispatcher) {
            authRepository.signUp(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                password = password,
                repeatPassword = repeatPassword
            )
        }
    }
}