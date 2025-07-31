package ru.itis.t_trips.looktripmembers.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.looktripmembers.LookTripMembersScreenEffect
import ru.itis.t_trips.looktripmembers.LookTripMembersScreenEvent
import ru.itis.t_trips.looktripmembers.LookTripMembersScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom

@Composable
fun LookTripMembersScreen(
    tripId: Int,
) {
    val viewModel: LookTripMembersViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is LookTripMembersScreenEffect.Message -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = LookTripMembersScreenState.Initial)
    when (pageState) {
        is LookTripMembersScreenState.Initial -> viewModel.processEvent(
            LookTripMembersScreenEvent.OnScreenInit(tripId)
        )
        is LookTripMembersScreenState.MembersResult, LookTripMembersScreenState.Loading -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_list_members),
            onBackPressed = {
                viewModel.processEvent(
                    LookTripMembersScreenEvent.OnBackBtnClick
                )
            },
        )
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (pageState) {
                is LookTripMembersScreenState.MembersResult -> {
                    val members = (pageState as? LookTripMembersScreenState.MembersResult)?.members ?: emptyList()
                    items(members) { contact ->
                        UserMiniCardCustom(
                            contact = contact,
                        )
                    }
                }
                is LookTripMembersScreenState.Loading -> {
                    items(10) {
                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.userMiniCardHeight)
                                .fillMaxWidth()
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}


@Composable
private fun rememberViewModel(): LookTripMembersViewModel = hiltViewModel()