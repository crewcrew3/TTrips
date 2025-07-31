package ru.itis.t_trips.navigation_api

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import ru.itis.t_trips.utils.ExceptionCode

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error(ExceptionCode.NAV_CONTROLLER_NOT_PROVIDED)
}