package ru.itis.t_trips.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.itis.profile.ui.ProfileScreen
import ru.itis.t_trips.actualexpenseinfo.ui.ActualExpenseInfoScreen
import ru.itis.t_trips.addactualexpense.addexpense.ui.AddActualExpenseScreen
import ru.itis.t_trips.addactualexpense.divideexpense.ui.DivideExpenseScreen
import ru.itis.t_trips.addplannedexpense.ui.AddPlannedExpenseScreen
import ru.itis.t_trips.addtrip.commoninfo.ui.AddTripScreen
import ru.itis.t_trips.addtrip.contacts.ui.ContactsScreen
import ru.itis.t_trips.authentication.password.ui.AuthenticationPasswordScreen
import ru.itis.t_trips.authentication.phonenumber.ui.AuthenticationPhoneNumberScreen
import ru.itis.t_trips.authentication.registration.ui.RegistrationScreen
import ru.itis.t_trips.editprofile.privacy.ui.PrivacyScreen
import ru.itis.t_trips.editprofile.profiledata.ui.EditProfileScreen
import ru.itis.t_trips.invitationdetails.ui.InvitationDetailsScreen
import ru.itis.t_trips.looktripmembers.ui.LookTripMembersScreen
import ru.itis.t_trips.navigation_api.route.ActualExpenseInfoRoute
import ru.itis.t_trips.navigation_api.route.AddActualExpenseRoute
import ru.itis.t_trips.navigation_api.route.AddPlannedExpenseRoute
import ru.itis.t_trips.navigation_api.route.AddTripRoute
import ru.itis.t_trips.navigation_api.route.AuthenticationPasswordRoute
import ru.itis.t_trips.navigation_api.route.AuthenticationPhoneNumberRoute
import ru.itis.t_trips.navigation_api.route.ContactsRoute
import ru.itis.t_trips.navigation_api.route.DivideExpenseRoute
import ru.itis.t_trips.navigation_api.route.EditProfileRoute
import ru.itis.t_trips.navigation_api.route.InvitationDetailsRoute
import ru.itis.t_trips.navigation_api.route.MembersTripRoute
import ru.itis.t_trips.navigation_api.route.NotificationsRoute
import ru.itis.t_trips.navigation_api.route.PrivacyRoute
import ru.itis.t_trips.navigation_api.route.ProfileRoute
import ru.itis.t_trips.navigation_api.route.RegistrationRoute
import ru.itis.t_trips.navigation_api.route.ReportTripRoute
import ru.itis.t_trips.navigation_api.route.TripArchiveRoute
import ru.itis.t_trips.navigation_api.route.TripInfoRoute
import ru.itis.t_trips.navigation_api.route.TripsListRoute
import ru.itis.t_trips.notifications.ui.NotificationsScreen
import ru.itis.t_trips.reporttrip.ui.ReportTripScreen
import ru.itis.t_trips.triparchive.ui.TripArchiveScreen
import ru.itis.t_trips.tripinfo.ui.TripInfoScreen
import ru.itis.triplist.ui.TripListScreen

@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    startDestination: Any,
) {
    //Лямбда, переданная в NavHost, в конечном итоге вызывает NavController.create Graph() и возвращает NavGraph.
    //NavGraph можно создать отдельно, а потом передать в NavHost
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        //вход+регистрация
        composable<AuthenticationPhoneNumberRoute> {
            AuthenticationPhoneNumberScreen()
        }
        composable<AuthenticationPasswordRoute> { backStackEntry ->
            val authPass : AuthenticationPasswordRoute = backStackEntry.toRoute()
            AuthenticationPasswordScreen(
                phoneNumber = authPass.phoneNumber
            )
        }
        composable<RegistrationRoute> { backStackEntry->
            val registration : RegistrationRoute = backStackEntry.toRoute()
            RegistrationScreen(
                phoneNumber = registration.phoneNumber
            )
        }

        //поездки
        composable<TripsListRoute> {
            TripListScreen()
        }
        composable<AddTripRoute> { backStackEntry ->
            val addTrip: AddTripRoute = backStackEntry.toRoute()
            AddTripScreen(
                contactsStr = addTrip.contacts
            )
        }
        composable<ContactsRoute> {
            ContactsScreen()
        }
        composable<TripInfoRoute> { backStackEntry ->
            val tripInfo: TripInfoRoute = backStackEntry.toRoute()
            TripInfoScreen(
                tripId = tripInfo.tripId
            )
        }
        composable<MembersTripRoute> { backStackEntry ->
            val memberTrip: MembersTripRoute = backStackEntry.toRoute()
            LookTripMembersScreen(
                tripId = memberTrip.tripId
            )
        }
        composable<ReportTripRoute> { backStackEntry ->
            val report: ReportTripRoute = backStackEntry.toRoute()
            ReportTripScreen(
                tripId = report.tripId
            )
        }

        //расходы
        composable<AddActualExpenseRoute> { backStackEntry ->
            val addActualExpenses: AddActualExpenseRoute = backStackEntry.toRoute()
            AddActualExpenseScreen(
                tripId = addActualExpenses.tripId,
                wayToDivideAmount = addActualExpenses.wayToDivideAmount,
                participantsAndAmountStr = addActualExpenses.participantsAndAmountStr
            )
        }
        composable<DivideExpenseRoute> { backStackEntry ->
            val divideExpense: DivideExpenseRoute = backStackEntry.toRoute()
            DivideExpenseScreen(
                tripId = divideExpense.tripId,
                participantsStr = divideExpense.participants,
                totalAmount = divideExpense.totalAmount
            )

        }
        composable<ActualExpenseInfoRoute> { backStackEntry ->
            val actualExpense: ActualExpenseInfoRoute = backStackEntry.toRoute()
            ActualExpenseInfoScreen(
                expenseId = actualExpense.expenseId,
                tripId = actualExpense.tripId
            )
        }
        composable<AddPlannedExpenseRoute> { backStackEntry ->
            val addPlannedExpenses: AddPlannedExpenseRoute = backStackEntry.toRoute()
            AddPlannedExpenseScreen(
                tripId = addPlannedExpenses.tripId,
            )
        }

        //профиль
        composable<ProfileRoute> {
            ProfileScreen()
        }
        composable<EditProfileRoute> { backStackEntry ->
            val editProfile: EditProfileRoute = backStackEntry.toRoute()
            EditProfileScreen(
                firstName = editProfile.firstName,
                lastName = editProfile.lastName,
                userPhotoUrl = editProfile.userPhotoUrl
            )
        }
        composable<TripArchiveRoute> {
            TripArchiveScreen()
        }
        composable<PrivacyRoute> { backStackEntry ->
            val privacy: PrivacyRoute = backStackEntry.toRoute()
            PrivacyScreen(
                phoneNumber = privacy.phoneNumber
            )
        }

        //уведы
        composable<NotificationsRoute> {
            NotificationsScreen()
        }
        composable<InvitationDetailsRoute> { backStackEntry ->
            val invitationDetails: InvitationDetailsRoute = backStackEntry.toRoute()
            InvitationDetailsScreen(
                id = invitationDetails.id,
                userId = invitationDetails.userId,
                tripId = invitationDetails.tripId
            )
        }
    }
}