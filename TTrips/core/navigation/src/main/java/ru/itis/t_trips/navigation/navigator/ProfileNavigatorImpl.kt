package ru.itis.t_trips.navigation.navigator

import ru.itis.t_trips.navigation.NavigatorDelegate
import ru.itis.t_trips.navigation_api.navigator.ProfileNavigator
import ru.itis.t_trips.navigation_api.route.AuthenticationPhoneNumberRoute
import ru.itis.t_trips.navigation_api.route.EditProfileRoute
import ru.itis.t_trips.navigation_api.route.PrivacyRoute
import ru.itis.t_trips.navigation_api.route.TripArchiveRoute
import javax.inject.Inject

internal class ProfileNavigatorImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate
) : ProfileNavigator {

    override fun toEditProfileScreen(firstName: String, lastName: String, userPhotoUrl: String?) {
        navigatorDelegate.navigate(route = EditProfileRoute(firstName = firstName, lastName = lastName, userPhotoUrl = userPhotoUrl))
    }

    override fun toTripArchiveScreen() {
        navigatorDelegate.navigate(route = TripArchiveRoute)
    }

    override fun toPrivacyScreen(phoneNumber: String) {
        navigatorDelegate.navigate(route = PrivacyRoute(phoneNumber))
    }

    override fun toAuthenticationPhoneNumberScreen() {
        navigatorDelegate.navigate(route = AuthenticationPhoneNumberRoute)
    }
}