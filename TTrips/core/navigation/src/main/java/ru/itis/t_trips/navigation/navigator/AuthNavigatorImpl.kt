package ru.itis.t_trips.navigation.navigator

import androidx.navigation.NavOptions
import ru.itis.t_trips.navigation.NavigatorDelegate
import ru.itis.t_trips.navigation_api.route.AuthenticationPasswordRoute
import ru.itis.t_trips.navigation_api.route.RegistrationRoute
import ru.itis.t_trips.navigation_api.route.TripsListRoute
import ru.itis.t_trips.navigation_api.navigator.AuthNavigator
import javax.inject.Inject

internal class AuthNavigatorImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : AuthNavigator {

    override fun toAuthenticationPasswordScreen(phoneNumber: String) {
        navigatorDelegate.navigate(route = AuthenticationPasswordRoute(phoneNumber))
    }

    override fun toRegistrationScreen(phoneNumber: String) {
        navigatorDelegate.navigate(route = RegistrationRoute(phoneNumber))
    }

    override fun toTripsListScreen() {
        val navController = navigatorDelegate.getNavController() ?: return
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, inclusive = true)
            .build()
        navigatorDelegate.navigate(route = TripsListRoute, navOptions = navOptions)
    }
}