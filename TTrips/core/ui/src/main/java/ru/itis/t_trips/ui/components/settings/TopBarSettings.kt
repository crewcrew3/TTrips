package ru.itis.t_trips.ui.components.settings

class TopBarSettings(
    val topAppBarText: String,
    val isIcon: Boolean = true,
    val onBackPressed: () -> Unit = {}
)