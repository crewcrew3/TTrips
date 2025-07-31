package ru.itis.t_trips.navigation_api.navigator

interface ProfileNavigator {
    fun toEditProfileScreen(firstName: String, lastName: String, userPhotoUrl: String?)
    fun toTripArchiveScreen()
    fun toPrivacyScreen(phoneNumber: String)
    fun toAuthenticationPhoneNumberScreen()
}