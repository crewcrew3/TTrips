package ru.itis.t_trips.reporttrip.ui

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
import ru.itis.t_trips.domain.usecase.report.GetTripReportUseCase
import ru.itis.t_trips.domain.usecase.report.GetUsersDebtsUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.reporttrip.ReportTripScreenEffect
import ru.itis.t_trips.reporttrip.ReportTripScreenEvent
import ru.itis.t_trips.reporttrip.ReportTripScreenState
import ru.itis.t_trips.ui.model.Contact
import javax.inject.Inject

@HiltViewModel
internal class ReportTripViewModel @Inject constructor(
    private val getUsersDebtsUseCase: GetUsersDebtsUseCase,
    private val getTripReportUseCase: GetTripReportUseCase,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<ReportTripScreenState>(value = ReportTripScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<ReportTripScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: ReportTripScreenEvent) {
        when (event) {
            is ReportTripScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is ReportTripScreenEvent.OnScreenInit -> loadReport(event.tripId)
            is ReportTripScreenEvent.OnUserCardClick -> getUserDebts(
                userId = event.userId,
                tripId = event.tripId,
            )
        }
    }

    private fun loadReport(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                getTripReportUseCase(tripId)
            }.onSuccess { report ->
                _pageState.value = ReportTripScreenState.Result(
                    trip = report.trip,
                    debtsMap = report.debtsMap.map { entry ->
                        val userModel = entry.key
                        Contact(
                            id = userModel.id,
                            name = userModel.firstName + " " + userModel.lastName,
                            phoneNumber = userModel.phoneNumber,
                        ) to entry.value
                    }.toMap()
                )
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    ReportTripScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun getUserDebts(tripId: Int, userId: Int) {
        viewModelScope.launch {
            runCatching {
                _pageState.value = ReportTripScreenState.Loading
                //delay(2000)
                val youOwe = getUsersDebtsUseCase(
                    tripId = tripId,
                    userId = userId,
                    isDebtor = true,
                )

                val youWasOwen = getUsersDebtsUseCase(
                    tripId = tripId,
                    userId = userId,
                    isDebtor = false,
                )

                youOwe to youWasOwen
            }.onSuccess { (youOwe, youWasOwen) ->
                _pageState.value = ReportTripScreenState.ResultDebts(
                    debtsListOwe = youOwe,
                    debtsListOwen = youWasOwen
                )
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    ReportTripScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }
}