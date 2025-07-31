package ru.itis.t_trips.domain.usecase.trip

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.TripModel
import ru.itis.t_trips.domain.repository.TripRepository
import javax.inject.Inject

class AddTripUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        title: String,
        startDate: String,
        endDate: String,
        budget: Double,
    ): TripModel {
        return withContext(dispatcher) {
            tripRepository.addTrip(
                title = title,
                startDate = startDate,
                endDate = endDate,
                budget = budget
            )
        }
    }
}