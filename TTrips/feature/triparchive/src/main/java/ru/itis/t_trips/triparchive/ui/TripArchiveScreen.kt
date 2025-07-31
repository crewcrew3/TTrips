package ru.itis.t_trips.triparchive.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.domain.model.TripModel
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.triparchive.TripArchiveEffect
import ru.itis.t_trips.triparchive.TripArchiveEvent
import ru.itis.t_trips.triparchive.TripArchiveState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.TripListItem
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.components.settings.TripListItemSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun TripArchiveScreen(
    list: List<TripModel> = emptyList(),
) {

    val viewModel: TripArchiveViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is TripArchiveEffect.Error -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = TripArchiveState.Initial)
    when (pageState) {
        is TripArchiveState.Initial -> viewModel.processEvent(
            TripArchiveEvent.OnScreenInit
        )

        is TripArchiveState.TripsResult, TripArchiveState.Loading -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.profile_screen_tab_archive),
            onBackPressed = {
                viewModel.processEvent(TripArchiveEvent.OnBackBtnClick)
            }
        )
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 20.dp)
        ) {
            when (pageState) {
                is TripArchiveState.Loading -> {
                    items(viewModel.numberOfLoadingItems) {
                        ShimmerCustom(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DimensionsCustom.tripCardHeight)
                                .padding(8.dp)
                        )
                    }
                }

                is TripArchiveState.TripsResult -> {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.archive_screen_text),
                                style = StylesCustom.body3,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier
                                    .padding(bottom = 20.dp)
                            )
                        }
                    }
                    val list = (pageState as TripArchiveState.TripsResult).result
                    items(list) { trip ->
                        Box {
                            TripListItem(
                                itemSettings = TripListItemSettings(
                                    id = trip.id,
                                    title = trip.title,
                                    status = trip.status,
                                    startDate = trip.startDate,
                                    endDate = trip.endDate,
                                    picUrl = trip.picUrl
                                ),
                                onClick = {
                                    viewModel.processEvent(
                                        TripArchiveEvent.OnItemClick(
                                            tripId = trip.id
                                        )
                                    )
                                }
                            )
                            Icon(
                                imageVector = IconsCustom.deleteIcon(),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(end = 16.dp, bottom = 24.dp)
                                    .align(Alignment.BottomEnd)
                                    .height(DimensionsCustom.iconSize)
                                    .width(DimensionsCustom.iconSize)
                                    .clickable {
                                        viewModel.processEvent(
                                            TripArchiveEvent.OnDeleteTripBtnClick(
                                                tripId = trip.id
                                            )
                                        )
                                    }
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun rememberViewModel(): TripArchiveViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun TripsArchiveScreenPreview() {
    TTripsTheme {
        TripArchiveScreen(
            list = listOf(
                TripModel(
                    id = 1,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Завершена",
                    budget = 1000.0,
                ),
                TripModel(
                    id = 2,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Завершена",
                    budget = 1000.0,
                ),
                TripModel(
                    id = 3,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Завершена",
                    budget = 1000.0,
                ),
                TripModel(
                    id = 4,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Завершена",
                    budget = 1000.0,
                ),
            )
        )
    }
}