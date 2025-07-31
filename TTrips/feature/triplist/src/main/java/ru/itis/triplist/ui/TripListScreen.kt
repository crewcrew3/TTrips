package ru.itis.triplist.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.TripListItem
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.components.settings.TripListItemSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme
import ru.itis.triplist.TripListScreenEffect
import ru.itis.triplist.TripListScreenEvent
import ru.itis.triplist.TripListScreenState

@Composable
fun TripListScreen(
    previewList : List<TripModel> = emptyList(),
) {

    val viewModel: TripListViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is TripListScreenEffect.Error -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = TripListScreenState.Initial)
    when (pageState) {
        is TripListScreenState.Initial -> viewModel.processEvent(TripListScreenEvent.OnScreenInit)
        is TripListScreenState.TripsResult, TripListScreenState.Loading -> Unit
    }

    BaseScreen(
        isBottomBar = true,
        isFloatingActionButton = true,
        floatingActBtnSettings = FloatingActionButtonSettings.CircleButtonSettings(
            icon = IconsCustom.addContentIcon(),
            onClick = {
                viewModel.processEvent(
                    TripListScreenEvent.OnAddTripBtnClick
                )
            },
        )
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = stringResource(R.string.title_my_trips),
                    style = StylesCustom.h5,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 24.dp)
                )
            }
            when (pageState) {
                is TripListScreenState.Loading -> {
                    items(viewModel.numberOfLoadingItems) {
                        ShimmerCustom(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DimensionsCustom.tripCardHeight)
                                .padding(8.dp)
                        )
                    }
                }

                is TripListScreenState.TripsResult -> {
                    val list = (pageState as TripListScreenState.TripsResult).result
                    items(list) { trip ->
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
                                    TripListScreenEvent.OnItemClick(
                                        tripId = trip.id
                                    )
                                )
                            }
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun rememberViewModel(): TripListViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun TripsListScreenPreview() {
    TTripsTheme {
        TripListScreen(
            previewList = listOf(
                TripModel(
                    id = 1,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Активна",
                    budget = 1000.0
                ),
                TripModel(
                    id = 2,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Активна",
                    budget = 1000.0
                ),
                TripModel(
                    id = 3,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Активна",
                    budget = 1000.0
                ),
                TripModel(
                    id = 4,
                    title = "Сочи",
                    startDate = "09.04.2025",
                    endDate = "12.04.1025",
                    status = "Активна",
                    budget = 1000.0
                ),
            )
        )
    }
}