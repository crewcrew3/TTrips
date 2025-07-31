package ru.itis.t_trips.domain.usecase.report

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.DebtModel
import ru.itis.t_trips.domain.repository.TripRepository
import javax.inject.Inject

class GetUsersDebtsUseCase @Inject constructor(
    private val tripRepository: TripRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int, userId: Int, isDebtor: Boolean): List<DebtModel> {
        return withContext(dispatcher) {
            tripRepository.getUsersDebts(
                tripId = tripId,
                userId = userId,
                isDebtor = isDebtor,
            )
        }
    }
}