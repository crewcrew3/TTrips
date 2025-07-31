package ru.itis.t_trips.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.settings.IconSettings
import ru.itis.t_trips.ui.components.settings.TripListItemSettings
import ru.itis.t_trips.ui.model.enums.TripStatus
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme
import ru.itis.t_trips.utils.toUiDate

//в списке поездок + в архиве поездок
@Composable
fun TripListItem(
    itemSettings: TripListItemSettings,
    isIcon: Boolean = false,
    iconSettings: IconSettings? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .height(DimensionsCustom.tripCardHeight)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (itemSettings.picUrl != null) {
                    ImageCustom(
                        url = itemSettings.picUrl,
                        modifier = Modifier
                            .height(DimensionsCustom.tripPicHeight)
                            .width(DimensionsCustom.tripPicWidth)
                    )
                } else {
                    ImageCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.tripPicHeight)
                            .width(DimensionsCustom.tripPicWidth)
                    )
                    Icon(
                        imageVector = IconsCustom.sunnyIcon(),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                        modifier = Modifier
                            .height(DimensionsCustom.iconSizeMaxi)
                            .width(DimensionsCustom.iconSizeMaxi)
                    )
                }
            }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = itemSettings.title,
                        style = StylesCustom.h2,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                    )
                    Text(
                        text = stringResource(TripStatus.getStringResourceId(itemSettings.status) ?: R.string.trip_status_unknown),
                        style = StylesCustom.body2,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = itemSettings.startDate.toUiDate() + " - " + itemSettings.endDate.toUiDate(),
                        style = StylesCustom.body4Light,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                    if (isIcon && iconSettings != null) {
                        Icon(
                            imageVector = iconSettings.icon,
                            tint = iconSettings.iconTint,
                            contentDescription = iconSettings.description,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .height(DimensionsCustom.iconSize)
                                .width(DimensionsCustom.iconSize)
                                .clickable { iconSettings.onClick() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun TripsListItemPreview() {
    TTripsTheme {
        TripListItem(
            itemSettings = TripListItemSettings(
                id = 1,
                title = "Сочи",
                startDate = "09.04.2025",
                endDate = "12.04.1025",
                status = "ACTIVE",
            ),
            isIcon = true,
            iconSettings = IconSettings(
                icon = IconsCustom.editIcon(),
                iconTint = MaterialTheme.colorScheme.secondary
            )
        )
    }
}
