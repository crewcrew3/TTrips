package ru.itis.t_trips.navigation_api.navigator

interface TripNavigator {
    fun toAddTripScreen(contacts: String = "")
    fun toContactsScreen()
    fun toTripDetailsScreen(tripId: Int)
    fun toEditTripScreen(tripId: Int)
    fun toActualExpenseInfoScreen(expenseId: Int, tripId: Int)
    fun toAddPlannedExpense(tripId: Int)
    fun toAddActualExpense(tripId: Int, wayToDivideAmount: String = "", participants: String = "")
    fun toDivideExpenseScreen(participants: String, tripId: Int, totalAmount:Double)
    fun toReportScreen(tripId: Int)
}