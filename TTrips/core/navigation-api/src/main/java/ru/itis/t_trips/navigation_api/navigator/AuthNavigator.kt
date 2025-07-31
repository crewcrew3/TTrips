package ru.itis.t_trips.navigation_api.navigator

interface AuthNavigator {
    fun toAuthenticationPasswordScreen(phoneNumber: String)
    fun toRegistrationScreen(phoneNumber: String)
    fun toTripsListScreen()
}