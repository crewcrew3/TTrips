package ru.itis.t_trips.ui.components.settings

import androidx.compose.ui.graphics.vector.ImageVector

sealed class FloatingActionButtonSettings {

    class CircleButtonSettings(
        val onClick: () -> Unit,
        val icon: ImageVector,
        val contentDescription: String = "",
    ) : FloatingActionButtonSettings()

    class RectangleButtonSettings(
        val onClick: () -> Unit,
        val onBtnText: String,
    ) : FloatingActionButtonSettings()
}