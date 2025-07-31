package ru.itis.t_trips.navigation.navigator

import ru.itis.t_trips.navigation.NavigatorDelegate
import ru.itis.t_trips.navigation_api.navigator.NotificationNavigator
import ru.itis.t_trips.navigation_api.route.InvitationDetailsRoute
import javax.inject.Inject

internal class NotificationNavigatorImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : NotificationNavigator {

    override fun toInvitationDetailsScreen(id: Int, userId: Int, tripId: Int) {
        navigatorDelegate.navigate(
            route = InvitationDetailsRoute(
                id = id,
                userId = userId,
                tripId = tripId
            )
        )
    }

    override fun toReportDetailsScreen() {
        TODO("Not yet implemented")
    }
}