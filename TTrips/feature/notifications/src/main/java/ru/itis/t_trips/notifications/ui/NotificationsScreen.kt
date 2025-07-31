package ru.itis.t_trips.notifications.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.notifications.NotificationsScreenEffect
import ru.itis.t_trips.notifications.NotificationsScreenEvent
import ru.itis.t_trips.notifications.NotificationsScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.DividerCustom
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun NotificationsScreen() {

    val viewModel: NotificationsViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is NotificationsScreenEffect.Message -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = NotificationsScreenState.Initial)
    when (pageState) {
        is NotificationsScreenState.Initial -> viewModel.processEvent(
            NotificationsScreenEvent.OnScreenInit
        )
        is NotificationsScreenState.NotificationsResult, NotificationsScreenState.Loading -> Unit
    }

    BaseScreen(
        isBottomBar = true,
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = stringResource(R.string.top_bar_header_notifications),
                    style = StylesCustom.h5,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 24.dp)
                )
            }
            when (pageState) {
                is NotificationsScreenState.NotificationsResult -> {
                    val notifications = (pageState as? NotificationsScreenState.NotificationsResult)?.notifications ?: emptyList()
                    items(notifications) { notification ->
                        NotificationItem(
                            onClick = {
                                viewModel.processEvent(
                                    NotificationsScreenEvent.onNotificationClick(
                                        notification = notification
                                    )
                                )
                            }
                        )
                        DividerCustom()
                    }
                }

                is NotificationsScreenState.Loading -> {
                    items(10) {
                        ShimmerCustom(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DimensionsCustom.notificationCardHeight)
                        )
                        DividerCustom()
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun NotificationItem(
    onClick: () -> Unit
) {
   Card(
       modifier = Modifier
           .fillMaxWidth()
           .height(DimensionsCustom.notificationCardHeight)
           .clickable { onClick() },
       colors = CardDefaults.cardColors(
           containerColor = Color.Transparent
       )
   ) {
       Row(
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier
               .fillMaxWidth(),
       ) {
           Column {
               Text(
                   text = stringResource(R.string.notification_invitation_title),
                   style = StylesCustom.h2,
                   color = MaterialTheme.colorScheme.onPrimary,
               )
               Spacer(modifier = Modifier.height(8.dp))
               Text(
                   text = stringResource(R.string.notification_invitation_subtitle),
                   style = StylesCustom.body5,
                   color = MaterialTheme.colorScheme.onTertiary,
               )
           }

           Icon(
               imageVector = IconsCustom.indicatorIcon(),
               tint = MaterialTheme.colorScheme.secondary,
               contentDescription = "",
               modifier = Modifier
                   .size(DimensionsCustom.iconSizeMini)
           )
       }
   }
}

@Composable
private fun rememberViewModel(): NotificationsViewModel = hiltViewModel()