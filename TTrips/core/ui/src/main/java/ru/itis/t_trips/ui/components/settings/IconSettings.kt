package ru.itis.t_trips.ui.components.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

class IconSettings(
    val icon: ImageVector,
    val onClick: () -> Unit = {},
    val description: String = "",
    val iconTint: Color,
)