package ru.itis.t_trips.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import ru.itis.t_trips.navigation_api.LocalNavController
import ru.itis.t_trips.ui.components.FloatingActionButtonCustom
import ru.itis.t_trips.ui.components.TopBarCustom
import ru.itis.t_trips.ui.components.ApplicationBottomNavigation
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom

@Composable
fun BaseScreen(
    isTopBar: Boolean = false,
    isBottomBar: Boolean = false,
    isFloatingActionButton: Boolean = false,
    topBarSettings: TopBarSettings? = null,
    floatingActBtnSettings: FloatingActionButtonSettings? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val systemInsets = WindowInsets.systemBars
    val customInsets = WindowInsets(left = DimensionsCustom.baseInsets, right = DimensionsCustom.baseInsets)
    Scaffold(
        contentWindowInsets = systemInsets.union(customInsets),
        topBar = { if (isTopBar && topBarSettings != null) TopBarCustom(settings = topBarSettings) },
        bottomBar = {
            if (isBottomBar) {
                val navController = LocalNavController.current
                ApplicationBottomNavigation(navController)
            }
        },
        floatingActionButton = {
            if (isFloatingActionButton && floatingActBtnSettings != null) {
                when(floatingActBtnSettings) {
                    is FloatingActionButtonSettings.CircleButtonSettings -> FloatingActionButtonCustom(settings = floatingActBtnSettings)
                    is FloatingActionButtonSettings.RectangleButtonSettings -> {
                        PrimaryButtonCustom(
                            onBtnText = floatingActBtnSettings.onBtnText,
                            onClick = floatingActBtnSettings.onClick
                        )
                    }
                }
            }
        },
        content = content,
    )
}