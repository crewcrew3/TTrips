package ru.itis.t_trips.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
    primary = ColorsCustom.LightPrimary,
    onPrimary = ColorsCustom.LightOnPrimary,
    secondary = ColorsCustom.LightSecondary,
    onSecondary = ColorsCustom.LightOnSecondary,
    tertiary = ColorsCustom.LightTertiary,
    onTertiary = ColorsCustom.LightOnTertiary,
    outline = ColorsCustom.LightOutline,
    outlineVariant = ColorsCustom.LightOutlineVariant,
    background = ColorsCustom.LightBackground,
    error = ColorsCustom.LightError,
    surfaceVariant = ColorsCustom.LightSurfaceVariant,
    onSurfaceVariant = ColorsCustom.LightOnSurfaceVariant,
    surface = ColorsCustom.LightSurface,
    onSurface = ColorsCustom.LightOnSurface,
    surfaceContainer = ColorsCustom.LightSurfaceContainer,
    surfaceContainerLow = ColorsCustom.LightSurfaceContainerLow,
    surfaceContainerHigh = ColorsCustom.LightSurfaceContainerHigh,
    surfaceBright = ColorsCustom.LightSurfaceBright,
)

@Composable
fun TTripsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}