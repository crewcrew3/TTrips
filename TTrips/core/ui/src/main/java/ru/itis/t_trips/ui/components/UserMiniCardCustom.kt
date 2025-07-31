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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.itis.t_trips.ui.components.settings.IconSettings
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun UserMiniCardCustom(
    contact: Contact,
    icon: ImageVector = IconsCustom.profileIcon(),
    isSubtitle: Boolean = true,
    isIcon: Boolean = false,
    iconSettings: IconSettings? = null,
    onChosenChange: ((Boolean) -> Unit)? = null,
    cardHeight: Dp = DimensionsCustom.userMiniCardHeight,
    avatarSize: Dp = DimensionsCustom.userMiniCardPicSize,
    iconAvatarSize: Dp = DimensionsCustom.iconSizeMaxi,
    textStyle: TextStyle = StylesCustom.h4,
    onClick: () -> Unit = {}
) {

    var isChosen by remember { mutableStateOf(false) }
    //val isChosen = contact.isChosen

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable {
                if (onChosenChange != null) {
                    isChosen = !isChosen
                    //contact.isChosen = !contact.isChosen
                    onChosenChange.invoke(isChosen)
                } else {
                    onClick()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    ImageCustom(
                        imageShape = CircleShape,
                        modifier = Modifier
                            .size(avatarSize)
                    )
                    Icon(
                        imageVector = icon,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                        modifier = Modifier
                            .size(iconAvatarSize)
                    )
                }
                if (isChosen) {
                    Icon(
                        imageVector = IconsCustom.checkIcon(),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = "",
                        modifier = Modifier
                            .height(DimensionsCustom.iconSizeMini)
                            .width(DimensionsCustom.iconSizeMini)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = contact.name,
                        style = textStyle,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                    if (isSubtitle) {
                        Text(
                            text = "+" + contact.phoneNumber,
                            style = StylesCustom.body2,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.padding(top = 8.dp, start = 20.dp)
                        )
                    }
                }
                if (isIcon && iconSettings != null) {
                    Icon(
                        imageVector = iconSettings.icon,
                        tint = iconSettings.iconTint,
                        contentDescription = iconSettings.description,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(DimensionsCustom.iconSizeMini)
                            .width(DimensionsCustom.iconSizeMini)
                            .clickable { iconSettings.onClick() }
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun UserMiniCardCustomPreview() {
    TTripsTheme {
        UserMiniCardCustom(
            Contact(
                name = "Никнейм",
                phoneNumber = "880055553535",
            ),
            isIcon = true,
            //isSubtitle = false,
            iconSettings = IconSettings(
                icon = IconsCustom.crossIcon(),
                onClick = {},
                iconTint = MaterialTheme.colorScheme.onTertiary
            ),
        )
    }
}
