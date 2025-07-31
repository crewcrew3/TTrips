package ru.itis.t_trips.data.remote.mapper

import ru.itis.t_trips.domain.model.TripModel
import ru.itis.t_trips.network.model.response.trip.TripResponse
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

internal class TripResponseMapper @Inject constructor() {
    fun map(input: TripResponse?): TripModel {
        val response = requireNotNull(input) { ExceptionCode.TRIP_RESPONSE }
        return TripModel(
            id = requireNotNull(response.id) { ExceptionCode.TRIP_RESPONSE },
            title = requireNotNull(response.title) { ExceptionCode.TRIP_RESPONSE },
            status = requireNotNull(response.status) { ExceptionCode.TRIP_RESPONSE },
            startDate = requireNotNull(response.startDate) { ExceptionCode.TRIP_RESPONSE},
            endDate = requireNotNull(response.endDate) { ExceptionCode.TRIP_RESPONSE},
            budget = requireNotNull(response.budget) { ExceptionCode.TRIP_RESPONSE }
        )
    }
}