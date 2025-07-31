package ru.itis.t_trips.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.theme.IconsCustom
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.itis.t_trips.navigation_api.route.NotificationsRoute
import ru.itis.t_trips.navigation_api.route.ProfileRoute
import ru.itis.t_trips.navigation_api.route.TopLevelRoute
import ru.itis.t_trips.navigation_api.route.TripsListRoute
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun ApplicationBottomNavigation(
    navController: NavHostController
) {
    val topLevelRoutes = listOf(
        TopLevelRoute(stringResource(R.string.title_trips), TripsListRoute, IconsCustom.tripsIcon()),
        TopLevelRoute(stringResource(R.string.title_notifications), NotificationsRoute, IconsCustom.notificationsIcon()),
        TopLevelRoute(stringResource(R.string.title_profile), ProfileRoute, IconsCustom.profileIcon()),
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .height(DimensionsCustom.bottomNavHeight)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        //destination содержит конкретные данные о текущем экране:
        //route (маршрут)
        //id (уникальный идентификатор)
        //Иерархию (для вложенных графов)
        val currentDestination = navBackStackEntry?.destination
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            topLevelRoutes.forEach { topLevelRoute ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = topLevelRoute.icon,
                            contentDescription = topLevelRoute.name,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .height(DimensionsCustom.iconSizeMaxi)
                                .width(DimensionsCustom.iconSizeMaxi)
                        )
                    },
                    label = {
                        Text(
                            text = topLevelRoute.name,
                            style = StylesCustom.body4,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    //если в иерархии есть хотя бы 1 экран у которого рут совпадает с topLevelRoute.route мы его подсвечиваем
                    selected = currentDestination?.hierarchy?.any { destination ->
                        destination.route == topLevelRoute.route.toString().substringBefore('@')
                    } == true,
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            // чтобы избежать накопления экранов в стеке навигации при переключении вкладок в боттом нав
                            popUpTo(navController.graph.findStartDestination().id) {
                                //Сохраняет состояние экранов, которые удаляются из стека
                                saveState = true
                            }
                            // чтобы небыло кучи копий одного и того же экрана в стеке при выборе одной и той же вкладки
                            launchSingleTop = true
                            // Восстанавливает состояние при выборе ранее выбранного элемента
                            restoreState = true
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.secondary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun ApplicationBottomNavigationPreview() {
    TTripsTheme {
        //ApplicationBottomNavigation()
    }
}

