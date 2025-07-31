package ru.itis.t_trips.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun FloatingActionButtonCustom(
    settings: FloatingActionButtonSettings.CircleButtonSettings,
) {
    FloatingActionButton(
        onClick = { settings.onClick() },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = DimensionsCustom.baseElevation,
        ),
        shape = CircleShape,
        modifier = Modifier
            .width(DimensionsCustom.circleBtnSize)
            .height(DimensionsCustom.circleBtnSize)
    ) {
        Icon(
            imageVector = settings.icon,
            contentDescription = settings.contentDescription,
            modifier = Modifier
                .width(DimensionsCustom.iconSize)
                .height(DimensionsCustom.iconSize)
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun FloatingActionButtonCustomPreview() {
    TTripsTheme {
        FloatingActionButtonCustom(
            FloatingActionButtonSettings.CircleButtonSettings(
                onClick = {},
                icon = IconsCustom.arrowForwardIcon()
            )
        )
    }
}