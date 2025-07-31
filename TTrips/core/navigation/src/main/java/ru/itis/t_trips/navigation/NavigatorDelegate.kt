package ru.itis.t_trips.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NavigatorDelegate @Inject constructor(){

    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun getNavController(): NavController? {
        return navController
    }

    fun navigate(
        route: Any,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        navController?.navigate(route, navOptions, navigatorExtras)
    }

    fun popBackStack() {
        navController?.popBackStack()
    }
}