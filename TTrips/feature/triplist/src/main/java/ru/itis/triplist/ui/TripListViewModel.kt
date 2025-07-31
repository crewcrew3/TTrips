package ru.itis.triplist.ui

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
import ru.itis.t_trips.domain.usecase.GetPictureUseCase
import ru.itis.t_trips.domain.usecase.trip.GetTripsListUseCase
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.ui.model.TripWithPicture
import ru.itis.t_trips.utils.OtherProperties
import ru.itis.triplist.TripListScreenEffect
import ru.itis.triplist.TripListScreenEvent
import ru.itis.triplist.TripListScreenState
import javax.inject.Inject

@HiltViewModel
internal class TripListViewModel @Inject constructor(
    private val getTripsListUseCase: GetTripsListUseCase,
    private val getPictureUseCase: GetPictureUseCase,
    private val tripNavigator: TripNavigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<TripListScreenState>(value = TripListScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<TripListScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    var numberOfLoadingItems = 10

    fun processEvent(event: TripListScreenEvent) {
        when (event) {
            is TripListScreenEvent.OnScreenInit -> getTripList()
            is TripListScreenEvent.OnAddTripBtnClick -> tripNavigator.toAddTripScreen()
            is TripListScreenEvent.OnItemClick -> tripNavigator.toTripDetailsScreen(event.tripId)
        }
    }

    private fun getTripList() {
        //для теста
        //_pageState.value = TripListScreenState.TripsResult(result = getHardcodeList())
        viewModelScope.launch {
            runCatching {
                _pageState.value = TripListScreenState.Loading
                //delay(2000)
                getTripsListUseCase()
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
                _pageState.value = TripListScreenState.TripsResult(result = tripListWithPics)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    TripListScreenEffect.Error(
                        message = messageResId
                    )
                )
            }
        }.also { numberOfLoadingItems = 0 }
    }
}