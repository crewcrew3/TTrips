package ru.itis.t_trips.domain.usecase.invitation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.model.notification.InvitationItemModel
import ru.itis.t_trips.domain.repository.InvitationsRepository
import javax.inject.Inject

class GetUsersInvitationsUseCase @Inject constructor(
    private val invitationsRepository: InvitationsRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): List<InvitationItemModel>  {
        return withContext(dispatcher) {
            invitationsRepository.getUsersInvitations()
        }
    }
}