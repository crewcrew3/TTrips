package ru.itis.t_trips.triparchive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.GetPictureUseCase
import ru.itis.t_trips.domain.usecase.trip.DeleteTripUseCase
import ru.itis.t_trips.domain.usecase.trip.GetTripsListUseCase
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.triparchive.TripArchiveEffect
import ru.itis.t_trips.triparchive.TripArchiveEvent
import ru.itis.t_trips.triparchive.TripArchiveState
import ru.itis.t_trips.ui.model.TripWithPicture
import ru.itis.t_trips.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
internal class TripArchiveViewModel @Inject constructor(
    private val getTripsListUseCase: GetTripsListUseCase,
    private val deleteTripUseCase: DeleteTripUseCase,
    private val getPictureUseCase: GetPictureUseCase,
    private val tripNavigator: TripNavigator,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<TripArchiveState>(value = TripArchiveState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<TripArchiveEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    var numberOfLoadingItems = 5

    fun processEvent(event: TripArchiveEvent) {
        when (event) {
            is TripArchiveEvent.OnScreenInit -> getTripArchive()
            is TripArchiveEvent.OnDeleteTripBtnClick -> deleteItem(event.tripId)
            is TripArchiveEvent.OnBackBtnClick -> navigator.popBackStack()
            is TripArchiveEvent.OnItemClick -> tripNavigator.toReportScreen(tripId = event.tripId)
        }
    }

    private fun getTripArchive() {
        //для теста
        //_pageState.value = TripArchiveState.TripsResult(result = getHardcodeList())
        viewModelScope.launch {
            runCatching {
                _pageState.value = TripArchiveState.Loading
                //delay(2000)
                getTripsListUseCase(onlyArchive = true)
            }.onSuccess { result ->
                val tripListWithPics = result.map { tripModel ->
                    TripWithPicture(
                        id = tripModel.id,
                        title = tripModel.title,
                        status = tripModel.status,
                        startDate = tripModel.startDate,
                        endDate = tripModel.endDate,
                        budget = tripModel.budget,
                        picUrl = getPictureUseCase(
                            keyPrefix = OtherProperties.FILE_TRIP_PIC_PREFIX,
                            imageId = tripModel.id
                        )
                    )
                }
                _pageState.value = TripArchiveState.TripsResult(result = tripListWithPics)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    TripArchiveEffect.Error(
                        message = messageResId
                    )
                )
            }.also { numberOfLoadingItems = 0 }
        }
    }

    private fun deleteItem(tripId: Int) {
        viewModelScope.launch {
            runCatching {
                deleteTripUseCase(tripId)
            }.onSuccess {
                _pageState.update { currentState ->
                    if (currentState is TripArchiveState.TripsResult) {
                        val currentTrips = currentState.result
                        val updatedTrips =
                            currentTrips.filter { it.id != tripId }
                        currentState.copy(result =updatedTrips)
                    } else {
                        currentState
                    }
                }
                _pageEffect.emit(TripArchiveEffect.Error(R.string.msg_delete_data_success))
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    TripArchiveEffect.Error(
                        message = messageResId
                    )
                )
            }
        }
    }
}