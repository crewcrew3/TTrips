package ru.itis.t_trips.domain.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.UserProfileModel
import ru.itis.t_trips.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): UserProfileModel {
        return withContext(dispatcher) {
            userRepository.getUserProfile()
        }
    }
}