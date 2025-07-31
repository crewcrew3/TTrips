package ru.itis.t_trips.navigation_api.navigator

interface NotificationNavigator {
    fun toInvitationDetailsScreen(id: Int, userId: Int, tripId: Int)
    fun toReportDetailsScreen()
}