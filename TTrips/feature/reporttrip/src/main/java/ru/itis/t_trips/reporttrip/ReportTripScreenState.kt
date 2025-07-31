package ru.itis.t_trips.reporttrip


import ru.itis.t_trips.domain.model.DebtModel
import ru.itis.t_trips.domain.model.TripModel
import ru.itis.t_trips.ui.model.Contact

internal sealed interface ReportTripScreenState {
    data object Initial : ReportTripScreenState
    data object Loading : ReportTripScreenState
    data class Result(val trip: TripModel, val debtsMap: Map<Contact, Double>) : ReportTripScreenState
    data class ResultDebts(val debtsListOwe: List<DebtModel>, val debtsListOwen: List<DebtModel>) : ReportTripScreenState
}