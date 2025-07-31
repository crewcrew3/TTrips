package ru.itis.t_trips.domain.usecase.trip

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.UserProfileModel
import ru.itis.t_trips.domain.repository.TripRepository
import ru.itis.t_trips.domain.repository.UserRepository
import javax.inject.Inject

class GetTripMembersUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int): List<UserProfileModel> {
        return withContext(dispatcher) {
            tripRepository.getTripMembersIds(tripId).map { userId ->
                userRepository.getUserById(userId)
            }
        }
    }
}