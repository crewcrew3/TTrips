package ru.itis.t_trips.invitationdetails.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.invitationdetails.InvitationDetailsScreenEffect
import ru.itis.t_trips.invitationdetails.InvitationDetailsScreenEvent
import ru.itis.t_trips.invitationdetails.InvitationDetailsScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ImageCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.utils.toUiDate

@Composable
fun InvitationDetailsScreen(
    id: Int,
    userId: Int,
    tripId: Int
) {

    val viewModel: InvitationDetailsViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is InvitationDetailsScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = InvitationDetailsScreenState.Initial)
    when (pageState) {
        is InvitationDetailsScreenState.Initial -> viewModel.processEvent(
            InvitationDetailsScreenEvent.OnScreenInit(
                id = id,
                userId = userId,
                tripId = tripId
            )
        )

        is InvitationDetailsScreenState.Result, InvitationDetailsScreenState.Loading -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.notification_invitation_title),
            onBackPressed = {
                viewModel.processEvent(
                    InvitationDetailsScreenEvent.OnBackBtnClick
                )
            }
        )
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 16.dp)
        ) {
            when (pageState) {
                is InvitationDetailsScreenState.Result -> {
                    (pageState as? InvitationDetailsScreenState.Result)?.let { resultState ->
                        val invitation = resultState.invitation
                        val tripPicUrl = resultState.tripPicUrl
                        val invitor = invitation.inviterUser
                        val trip = invitation.trip

                        Text(
                            text = invitor.firstName + " " + invitor.lastName + " " + stringResource(
                                R.string.invitation_text
                            ),
                            style = StylesCustom.body3,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            if (tripPicUrl != null) {
                                ImageCustom(
                                    url = tripPicUrl,
                                    modifier = Modifier
                                        .height(DimensionsCustom.tripPicHeightMax)
                                        .fillMaxWidth(),
                                    imageShape = RoundedCornerShape(DimensionsCustom.roundedCorners)
                                )
                            } else {
                                ImageCustom(
                                    modifier = Modifier
                                        .height(DimensionsCustom.tripPicHeightMax)
                                        .fillMaxWidth()
                                )
                                Icon(
                                    imageVector = IconsCustom.sunnyIcon(),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(DimensionsCustom.iconSizeMax)
                                        .width(DimensionsCustom.iconSizeMax)
                                )
                            }
                        }

                        Text(
                            text = trip.title,
                            style = StylesCustom.h111,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 16.dp)
                        )

                        Text(
                            text = trip.startDate.toUiDate() + " - " + trip.endDate.toUiDate(),
                            style = StylesCustom.body3,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 20.dp)
                        )

                        Spacer(modifier = Modifier.height(200.dp))

                        Text(
                            text = stringResource(R.string.btn_look_members_text),
                            style = StylesCustom.body3Underline,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .clickable {
                                    viewModel.processEvent(
                                        InvitationDetailsScreenEvent.OnLookMembersBtnClick(
                                            tripId = trip.id
                                        )
                                    )
                                }
                        )

                        PrimaryButtonCustom(
                            onBtnText = stringResource(R.string.btn_accept_text),
                            onClick = {
                                viewModel.processEvent(
                                    InvitationDetailsScreenEvent.OnAcceptBtnClickInvitation(
                                        invitationId = invitation.id
                                    )
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PrimaryButtonCustom(
                            onBtnText = stringResource(R.string.btn_reject_text),
                            buttonColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            onClick = {
                                viewModel.processEvent(
                                    InvitationDetailsScreenEvent.OnRejectBtnClickInvitation(
                                        invitationId = invitation.id
                                    )
                                )
                            }
                        )
                    }
                }

                is InvitationDetailsScreenState.Loading -> {
                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.shimmerTextHeight)
                            .width(DimensionsCustom.shimmerTextWidth)
                            .padding(16.dp)
                    )

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.tripPicHeightMax)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(DimensionsCustom.roundedCorners))
                    )
                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.shimmerTextHeight)
                            .width(DimensionsCustom.shimmerTextWidth)
                            .padding(16.dp)
                    )

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.shimmerTextHeight)
                            .width(DimensionsCustom.shimmerTextWidth)
                            .padding(top = 20.dp)
                    )

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.shimmerTextHeight)
                            .width(DimensionsCustom.shimmerTextWidth)
                            .padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(200.dp))

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.btnHeight)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.btnHeight)
                            .fillMaxWidth()
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun rememberViewModel(): InvitationDetailsViewModel = hiltViewModel()