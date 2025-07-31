package ru.itis.t_trips.reporttrip.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.reporttrip.ReportTripScreenEffect
import ru.itis.t_trips.reporttrip.ReportTripScreenEvent
import ru.itis.t_trips.reporttrip.ReportTripScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun ReportTripScreen(
    tripId: Int
) {

    val viewModel: ReportTripViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is ReportTripScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = ReportTripScreenState.Initial)
    when (pageState) {
        is ReportTripScreenState.Initial -> viewModel.processEvent(
            ReportTripScreenEvent.OnScreenInit(
                tripId = tripId,
            )
        )

        is ReportTripScreenState.Result, ReportTripScreenState.Loading -> Unit
        is ReportTripScreenState.ResultDebts -> Unit
    }

    var showDialog by remember { mutableStateOf(false) }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_report),
            onBackPressed = {
                viewModel.processEvent(
                    ReportTripScreenEvent.OnBackBtnClick
                )
            }
        )
    ) { innerPadding ->
        when (pageState) {
            is ReportTripScreenState.Result -> {
                (pageState as? ReportTripScreenState.Result)?.let { resultState ->
                    val trip = resultState.trip
                    val debts = resultState.debtsMap
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(top = 40.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.report_title, trip.title),
                                style = StylesCustom.h11,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 40.dp)
                            )

                            Text(
                                text = stringResource(R.string.report_subtitle),
                                style = StylesCustom.body3,
                                color = MaterialTheme.colorScheme.onTertiary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 32.dp)
                            )

                            debts.forEach { (user, debt) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .width(224.dp)
                                    ) {
                                        UserMiniCardCustom(
                                            contact = user,
                                            isSubtitle = false,
                                            cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                            avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                            iconAvatarSize = DimensionsCustom.iconSize,
                                            textStyle = StylesCustom.body3,
                                            onClick = {
                                                showDialog = true
                                                viewModel.processEvent(
                                                    ReportTripScreenEvent.OnUserCardClick(
                                                        userId = user.id,
                                                        tripId = trip.id
                                                    )
                                                )
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(48.dp))
                                    Text(
                                        text = debt.toString() + " " + stringResource(R.string.rub),
                                        style = StylesCustom.body5,
                                        color = MaterialTheme.colorScheme.secondary,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is ReportTripScreenState.ResultDebts -> {
                (pageState as? ReportTripScreenState.ResultDebts)?.let { resultState ->
                    AnimatedCustomDialog(
                        show = showDialog,
                        onDismiss = {
                            showDialog = false
                            viewModel.processEvent(
                                ReportTripScreenEvent.OnScreenInit(
                                    tripId = tripId
                                )
                            )
                        }
                    ) {
                        LazyColumn(modifier = Modifier.padding(24.dp)) {
                            item {
                                Text(
                                    text = stringResource(R.string.debts),
                                    style = StylesCustom.h1,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                )
                            }

                            items(resultState.debtsListOwe) { debt ->
                                val creditor = debt.creditor
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .width(216.dp)
                                    ) {
                                        UserMiniCardCustom(
                                            contact = Contact(
                                                id = creditor.id,
                                                name = creditor.firstName + " " + creditor.lastName,
                                                phoneNumber = creditor.phoneNumber
                                            ),
                                            isSubtitle = false,
                                            cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                            avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                            iconAvatarSize = DimensionsCustom.iconSize,
                                            textStyle = StylesCustom.body3
                                        )
                                    }
                                    Text(
                                        text = stringResource(R.string.you_owe, debt.amount),
                                        style = StylesCustom.body4Light,
                                        color = MaterialTheme.colorScheme.error,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                    )
                                }
                            }

                            items(resultState.debtsListOwen) { debt ->
                                val debtor = debt.debtor
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .width(216.dp)
                                    ) {
                                        UserMiniCardCustom(
                                            contact = Contact(
                                                id = debtor.id,
                                                name = debtor.firstName + " " + debtor.lastName,
                                                phoneNumber = debtor.phoneNumber
                                            ),
                                            isSubtitle = false,
                                            cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                            avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                            iconAvatarSize = DimensionsCustom.iconSize,
                                            textStyle = StylesCustom.body3
                                        )
                                    }
                                    Text(
                                        text = stringResource(R.string.you_was_owen, debt.amount),
                                        style = StylesCustom.body4Light,
                                        color = Color.Green,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is ReportTripScreenState.Loading -> {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 40.dp)
                ) {
                    item {

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .width(DimensionsCustom.shimmerTextWidth)
                                .padding(bottom = 40.dp)
                        )

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .width(DimensionsCustom.shimmerTextWidth)
                                .padding(bottom = 32.dp)
                        )
                    }

                    items(5) {
                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.userMiniExtraCardHeight)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .width(DimensionsCustom.shimmerTextWidth)
                        )
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
fun AnimatedCustomDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn() + slideInVertically { -40 },
        exit = fadeOut() + slideOutVertically { -40 }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onDismiss)
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(horizontal = 20.dp)
                    .clickable { } // предотвращает закрытие при клике на контент
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun rememberViewModel(): ReportTripViewModel = hiltViewModel()