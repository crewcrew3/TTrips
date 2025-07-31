package ru.itis.t_trips.addactualexpense.divideexpense.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.t_trips.addactualexpense.divideexpense.DivideExpenseScreenEffect
import ru.itis.t_trips.addactualexpense.divideexpense.DivideExpenseScreenEvent
import ru.itis.t_trips.addactualexpense.divideexpense.DivideExpenseScreenState
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import javax.inject.Inject

@HiltViewModel
internal class DivideExpenseViewModel @Inject constructor(
    private val tripNavigator: TripNavigator,
    private val navigator: Navigator,
) : ViewModel() {

    private val _pageState = MutableStateFlow<DivideExpenseScreenState>(value = DivideExpenseScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<DivideExpenseScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: DivideExpenseScreenEvent) {
        when (event) {
            is DivideExpenseScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is DivideExpenseScreenEvent.OnNextBtnClick -> tripNavigator.toAddActualExpense(
                tripId = event.tripId,
                wayToDivideAmount = event.wayToDivideAmount,
                participants = Json.encodeToString(event.participants)
            )
        }
    }
}