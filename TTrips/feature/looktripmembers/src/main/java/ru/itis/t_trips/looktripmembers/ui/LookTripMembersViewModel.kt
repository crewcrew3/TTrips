package ru.itis.t_trips.looktripmembers.ui

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
import ru.itis.t_trips.domain.usecase.trip.GetTripMembersUseCase
import ru.itis.t_trips.looktripmembers.LookTripMembersScreenEffect
import ru.itis.t_trips.looktripmembers.LookTripMembersScreenEvent
import ru.itis.t_trips.looktripmembers.LookTripMembersScreenState
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.ui.model.Contact
import javax.inject.Inject

@HiltViewModel
internal class LookTripMembersViewModel @Inject constructor(
    private val getTripMembersUseCase: GetTripMembersUseCase,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<LookTripMembersScreenState>(value = LookTripMembersScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<LookTripMembersScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: LookTripMembersScreenEvent) {
        when (event) {
            is LookTripMembersScreenEvent.OnScreenInit -> loadMembers(event.tripId)
            is LookTripMembersScreenEvent.OnBackBtnClick -> navigator.popBackStack()
        }
    }

    private fun loadMembers(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                _pageState.value = LookTripMembersScreenState.Loading
                //delay(2000)
                getTripMembersUseCase(tripId)
            }.onSuccess { result ->
                val members = result.map { userModel ->
                    Contact(
                        id = userModel.id,
                        name = userModel.firstName + " " + userModel.lastName,
                        phoneNumber = userModel.phoneNumber
                    )
                }
                _pageState.value = LookTripMembersScreenState.MembersResult(members = members)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    LookTripMembersScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}