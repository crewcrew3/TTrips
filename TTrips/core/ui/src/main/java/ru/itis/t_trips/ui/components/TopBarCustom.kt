package ru.itis.t_trips.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCustom(
    settings: TopBarSettings,
) {
    TopAppBar(
        title = {
            Text(
                text = settings.topAppBarText,
                style = StylesCustom.h11,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
            )
        },
        navigationIcon = {
            if (settings.isIcon) {
                IconButton(
                    onClick = settings.onBackPressed,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)

                ) {
                    Icon(
                        imageVector = IconsCustom.arrowBackIcon(),
                        contentDescription = stringResource(R.string.btn_back_description),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .height(DimensionsCustom.iconSize)
                            .width(DimensionsCustom.iconSize)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            //.height(64.dp)
    )
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun TopBarCustomPreview() {
    TTripsTheme {
        TopBarCustom(
            TopBarSettings(
                topAppBarText = "Заголовок",
                onBackPressed = {}
            )
        )
    }
}