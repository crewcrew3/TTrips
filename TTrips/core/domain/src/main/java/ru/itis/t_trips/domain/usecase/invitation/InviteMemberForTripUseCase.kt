package ru.itis.t_trips.domain.usecase.invitation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.TripRepository
import javax.inject.Inject

class InviteMemberForTripUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int, phoneNumbers: List<String>) {
        withContext(dispatcher) {
            phoneNumbers.forEach { tripRepository.inviteMemberForTrip(tripId = tripId, phoneNumber = it) }
        }
    }
}