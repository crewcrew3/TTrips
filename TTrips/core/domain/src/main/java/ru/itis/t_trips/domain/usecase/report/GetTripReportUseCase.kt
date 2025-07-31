package ru.itis.t_trips.domain.usecase.report

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.ReportModel
import ru.itis.t_trips.domain.repository.TripRepository
import javax.inject.Inject

class GetTripReportUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int): ReportModel {
        return withContext(dispatcher) {
            tripRepository.getTripReport(tripId)
        }
    }
}