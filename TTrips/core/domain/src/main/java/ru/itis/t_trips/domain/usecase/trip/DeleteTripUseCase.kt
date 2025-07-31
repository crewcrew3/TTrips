package ru.itis.t_trips.domain.usecase.trip

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.TripRepository
import javax.inject.Inject

class DeleteTripUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int) {
        withContext(dispatcher) {
            tripRepository.deleteTrip(tripId)
        }
    }
}