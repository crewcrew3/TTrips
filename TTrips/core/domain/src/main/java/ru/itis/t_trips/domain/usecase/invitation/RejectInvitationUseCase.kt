package ru.itis.t_trips.domain.usecase.invitation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.InvitationsRepository
import javax.inject.Inject

class RejectInvitationUseCase @Inject constructor(
    private val invitationsRepository: InvitationsRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(tripId: Int) {
        withContext(dispatcher) {
            invitationsRepository.rejectInvitation(tripId)
        }
    }
}