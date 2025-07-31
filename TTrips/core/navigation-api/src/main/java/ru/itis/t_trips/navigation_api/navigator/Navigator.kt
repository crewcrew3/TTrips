package ru.itis.t_trips.navigation_api.navigator

import androidx.navigation.NavController
import ru.itis.t_trips.navigation_api.route.ProfileRoute

interface Navigator {
    fun setNavController(navController: NavController)
    fun getNavController(): NavController?
    fun popBackStack()
    fun toAuthScreen()
    fun toMembersTripScreen(tripId: Int)
    fun toProfileScreen()
}