package ru.itis.t_trips.navigation.navigator

import androidx.navigation.NavOptions
import ru.itis.t_trips.navigation.NavigatorDelegate
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.navigation_api.route.ActualExpenseInfoRoute
import ru.itis.t_trips.navigation_api.route.AddActualExpenseRoute
import ru.itis.t_trips.navigation_api.route.AddPlannedExpenseRoute
import ru.itis.t_trips.navigation_api.route.AddTripRoute
import ru.itis.t_trips.navigation_api.route.ContactsRoute
import ru.itis.t_trips.navigation_api.route.DivideExpenseRoute
import ru.itis.t_trips.navigation_api.route.EditTripRoute
import ru.itis.t_trips.navigation_api.route.ProfileRoute
import ru.itis.t_trips.navigation_api.route.ReportTripRoute
import ru.itis.t_trips.navigation_api.route.TripInfoRoute
import ru.itis.t_trips.navigation_api.route.TripsListRoute
import javax.inject.Inject

internal class TripNavigatorImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : TripNavigator {

    override fun toAddTripScreen(contacts: String) {
        val navOptions = NavOptions.Builder() //чтобы на экарн контактов не возвращались
            .setPopUpTo(TripsListRoute, inclusive = false)
            .build()
        navigatorDelegate.navigate(
            route = AddTripRoute(contacts = contacts),
            navOptions = navOptions
        )
    }

    override fun toContactsScreen() {
        navigatorDelegate.navigate(route = ContactsRoute)
    }

    override fun toTripDetailsScreen(tripId: Int) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(TripsListRoute, inclusive = false)
            .build()
        navigatorDelegate.navigate(
            route = TripInfoRoute(tripId = tripId),
            navOptions = navOptions,
        )
    }

    override fun toEditTripScreen(tripId: Int) {
        navigatorDelegate.navigate(route = EditTripRoute(tripId = tripId))
    }

    override fun toActualExpenseInfoScreen(expenseId: Int, tripId: Int) {
        navigatorDelegate.navigate(
            route = ActualExpenseInfoRoute(expenseId = expenseId, tripId = tripId)
        )
    }

    override fun toAddPlannedExpense(tripId: Int) {
        navigatorDelegate.navigate(route = AddPlannedExpenseRoute(tripId = tripId))
    }

    override fun toAddActualExpense(tripId: Int, wayToDivideAmount: String, participants: String) {
        navigatorDelegate.navigate(
            route = AddActualExpenseRoute(
                tripId = tripId,
                wayToDivideAmount = wayToDivideAmount,
                participantsAndAmountStr = participants
            )
        )
    }

    override fun toDivideExpenseScreen(participants: String, tripId: Int, totalAmount: Double) {
        navigatorDelegate.navigate(route = DivideExpenseRoute(participants = participants, tripId = tripId, totalAmount = totalAmount))
    }

    override fun toReportScreen(tripId: Int) {
        navigatorDelegate.navigate(route = ReportTripRoute(tripId = tripId))
    }
}