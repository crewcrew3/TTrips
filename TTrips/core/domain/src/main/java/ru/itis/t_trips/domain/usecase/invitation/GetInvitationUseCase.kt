package ru.itis.t_trips.domain.usecase.invitation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.InvitationModel
import ru.itis.t_trips.domain.repository.InvitationsRepository
import javax.inject.Inject

class GetInvitationUseCase @Inject constructor(
    private val invitationsRepository: InvitationsRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(id: Int, userId: Int, tripId: Int): InvitationModel {
        return withContext(dispatcher) {
            invitationsRepository.getInvitationInfo(
                invitationId = id,
                userId = userId,
                tripId = tripId
            )
        }
    }
}