package ru.itis.t_trips.navigation.navigator

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.itis.t_trips.navigation.NavigatorDelegate
import ru.itis.t_trips.navigation_api.navigator.AuthNavigator
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.NotificationNavigator
import ru.itis.t_trips.navigation_api.navigator.ProfileNavigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import ru.itis.t_trips.navigation_api.route.AuthenticationPhoneNumberRoute
import ru.itis.t_trips.navigation_api.route.MembersTripRoute
import ru.itis.t_trips.navigation_api.route.ProfileRoute
import javax.inject.Inject

internal class NavigatorImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
    private val authNavigator: AuthNavigator,
    private val tripNavigator: TripNavigator,
    private val profileNavigator: ProfileNavigator,
    private val notificationNavigator: NotificationNavigator,
) : Navigator,
    AuthNavigator by authNavigator,
    TripNavigator by tripNavigator,
    ProfileNavigator by profileNavigator,
    NotificationNavigator by notificationNavigator {

    override fun setNavController(navController: NavController) {
        navigatorDelegate.setNavController(navController = navController)
    }

    override fun getNavController(): NavController? {
        return navigatorDelegate.getNavController()
    }

    override fun popBackStack() {
        navigatorDelegate.popBackStack()
    }

    override fun toAuthScreen() {
        val navController = navigatorDelegate.getNavController() ?: return
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, inclusive = true)
            .build()
        navigatorDelegate.navigate(route = AuthenticationPhoneNumberRoute, navOptions = navOptions)
    }

    override fun toMembersTripScreen(tripId: Int) {
        navigatorDelegate.navigate(route = MembersTripRoute(tripId = tripId))
    }

    override fun toProfileScreen() {
        navigatorDelegate.navigate(route = ProfileRoute)
    }
}