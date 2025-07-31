package ru.itis.t_trips.notifications.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.model.notification.InvitationItemModel
import ru.itis.t_trips.domain.model.notification.ReportItemModel
import ru.itis.t_trips.domain.usecase.invitation.GetUsersInvitationsUseCase
import ru.itis.t_trips.navigation_api.navigator.NotificationNavigator
import ru.itis.t_trips.notifications.NotificationsScreenEffect
import ru.itis.t_trips.notifications.NotificationsScreenEvent
import ru.itis.t_trips.notifications.NotificationsScreenState
import javax.inject.Inject

@HiltViewModel
internal class NotificationsViewModel @Inject constructor(
    private val getUsersInvitationsUseCase: GetUsersInvitationsUseCase,
    private val notificationNavigator: NotificationNavigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<NotificationsScreenState>(value = NotificationsScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<NotificationsScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: NotificationsScreenEvent) {
        when (event) {
            is NotificationsScreenEvent.OnScreenInit -> loadNotifications()
            is NotificationsScreenEvent.onNotificationClick -> {
                when (event.notification) {
                    is InvitationItemModel -> {
                        notificationNavigator.toInvitationDetailsScreen(
                            id = event.notification.id,
                            userId = event.notification.inviterId,
                            tripId = event.notification.tripId,
                        )
                    }

                    is ReportItemModel -> {
                        //к экрану с отчетом
                    }
                }
            }
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            runCatching {
                _pageState.value = NotificationsScreenState.Loading
                //delay(2000)
                getUsersInvitationsUseCase()
                //отчеты о поездке получаем здесь же? объединяем 2 списка с типом NotificationModel и отображаем
            }.onSuccess { notifications ->
                _pageState.value = NotificationsScreenState.NotificationsResult(notifications = notifications)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    NotificationsScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}