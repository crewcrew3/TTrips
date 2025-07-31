package ru.itis.t_trips.navigation_api.route

import kotlinx.serialization.Serializable

//вход+регистрация
@Serializable
object AuthenticationPhoneNumberRoute

@Serializable
data class AuthenticationPasswordRoute(val phoneNumber: String)

@Serializable
data class RegistrationRoute(val phoneNumber: String)

//поездки
@Serializable
object TripsListRoute

@Serializable
data class AddTripRoute(val contacts: String)

@Serializable
object ContactsRoute

@Serializable
data class TripInfoRoute(val tripId: Int)

@Serializable
data class EditTripRoute(val tripId: Int)

@Serializable
data class MembersTripRoute(val tripId: Int)

@Serializable
data class ReportTripRoute(val tripId: Int)

//расходы
@Serializable
data class ActualExpenseInfoRoute(val expenseId: Int, val tripId: Int)

@Serializable
data class AddActualExpenseRoute(val tripId: Int, val wayToDivideAmount: String, val participantsAndAmountStr: String)

@Serializable
data class DivideExpenseRoute(val participants: String, val tripId: Int, val totalAmount: Double)

@Serializable
data class AddPlannedExpenseRoute(val tripId: Int)

//профиль
@Serializable
object ProfileRoute

@Serializable
data class EditProfileRoute(val firstName: String, val lastName: String, val userPhotoUrl: String?)

@Serializable
object TripArchiveRoute

@Serializable
data class PrivacyRoute(val phoneNumber: String)

//уведы
@Serializable
object NotificationsRoute

@Serializable
data class InvitationDetailsRoute(val id: Int, val userId: Int, val tripId: Int)