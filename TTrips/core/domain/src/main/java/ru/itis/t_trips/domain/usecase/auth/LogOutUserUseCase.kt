package ru.itis.t_trips.domain.usecase.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Boolean {
        return withContext(dispatcher) {
            authRepository.logOut()
        }
    }
}