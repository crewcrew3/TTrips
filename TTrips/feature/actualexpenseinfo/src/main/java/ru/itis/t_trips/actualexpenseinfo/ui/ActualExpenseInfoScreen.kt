package ru.itis.t_trips.actualexpenseinfo.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.actualexpenseinfo.ActualExpenseInfoScreenEffect
import ru.itis.t_trips.actualexpenseinfo.ActualExpenseInfoScreenEvent
import ru.itis.t_trips.actualexpenseinfo.ActualExpenseInfoScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ImageCustom
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun ActualExpenseInfoScreen(
    expenseId: Int,
    tripId: Int
) {

    val viewModel: ActualExpenseInfoViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is ActualExpenseInfoScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = ActualExpenseInfoScreenState.Initial)
    when (pageState) {
        is ActualExpenseInfoScreenState.Initial -> viewModel.processEvent(
            ActualExpenseInfoScreenEvent.OnScreenInit(expenseId = expenseId)
        )

        is ActualExpenseInfoScreenState.Result, ActualExpenseInfoScreenState.Loading -> Unit
    }

    val permissionGallery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isCreator by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        viewModel.processEvent(
            ActualExpenseInfoScreenEvent.OnReceiptAdded(
                receipt = uri,
                expenseId = expenseId,
                context = context
            )
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        }
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_look_expense_info),
            onBackPressed = {
                viewModel.processEvent(
                    ActualExpenseInfoScreenEvent.OnBackBtnClick(
                        tripId = tripId
                    )
                )
            }
        ),
    ) { innerPadding ->

        when (pageState) {
            is ActualExpenseInfoScreenState.Result -> {
                (pageState as? ActualExpenseInfoScreenState.Result)?.let { resultState ->
                    val expense = resultState.expense
                    val userPaid = resultState.userPaid
                    val participants = resultState.participants
                    isCreator = resultState.isCreator
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(top = 48.dp)
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = expense.title,
                                    style = StylesCustom.h111,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                )
                                if (isCreator) {
                                    Icon(
                                        imageVector = IconsCustom.deleteIcon(),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .size(DimensionsCustom.iconSizeMaxi)
                                            .padding(end = 8.dp)
                                            .clickable {
                                                viewModel.processEvent(
                                                    ActualExpenseInfoScreenEvent.OnDeleteExpenseIconClick(
                                                        expenseId = expenseId,
                                                        tripId = tripId,
                                                    )
                                                )
                                            }
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = 32.dp)
                            ) {
                                Text(
                                    text = expense.amount.toString(),
                                    style = StylesCustom.h12,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Icon(
                                    imageVector = IconsCustom.rubIcon(),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .size(DimensionsCustom.iconSizeMaxi)
                                )
                            }

                            Text(
                                text = stringResource(R.string.label_receipt),
                                style = StylesCustom.body5,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            if (selectedImageUri != null) {
                                ImageCustom(
                                    url = selectedImageUri.toString(),
                                    modifier = Modifier
                                        .height(DimensionsCustom.tripPicHeightMax)
                                        .fillMaxWidth(),
                                    imageShape = RoundedCornerShape(DimensionsCustom.roundedCorners)
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clickable {
                                            if (ContextCompat.checkSelfPermission(
                                                    context,
                                                    permissionGallery
                                                ) == PackageManager.PERMISSION_GRANTED
                                            ) {
                                                pickImageLauncher.launch("image/*")
                                            } else {
                                                permissionLauncher.launch(permissionGallery)
                                            }
                                        }
                                ) {
                                    Icon(
                                        imageVector = IconsCustom.receiptIcon(),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .size(DimensionsCustom.iconSizeMaxi)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.label_add_receipt),
                                        style = StylesCustom.body5,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            UserMiniCardCustom(
                                contact = userPaid,
                                isSubtitle = false,
                                cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                iconAvatarSize = DimensionsCustom.iconSize,
                                textStyle = StylesCustom.body3
                            )

                            Text(
                                text = stringResource(R.string.label_paid_for),
                                style = StylesCustom.body5,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            )

                            LazyRow {
                                items(participants) { item ->
                                    UserMiniCardCustom(
                                        contact = item,
                                        isSubtitle = false,
                                        cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                        avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                        iconAvatarSize = DimensionsCustom.iconSize,
                                        textStyle = StylesCustom.body3
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = stringResource(R.string.label_way_to_divide_amount),
                                style = StylesCustom.body5,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                            )
                        }

                        items(participants) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .width(240.dp)
                                ) {
                                    UserMiniCardCustom(
                                        contact = item,
                                        isSubtitle = false,
                                        cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                        avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                        iconAvatarSize = DimensionsCustom.iconSize,
                                        textStyle = StylesCustom.body3
                                    )
                                }
                                Text(
                                    text = item.amount.toString() + " " + stringResource(R.string.rub),
                                    style = StylesCustom.body5,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .padding(start = 40.dp)
                                )
                            }
                        }
                    }
                }
            }

            is ActualExpenseInfoScreenState.Loading -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 48.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            ShimmerCustom(
                                modifier = Modifier
                                    .height(DimensionsCustom.shimmerTextHeight)
                                    .width(DimensionsCustom.shimmerTextWidth)
                                    .padding(bottom = 16.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                        ) {
                            ShimmerCustom(
                                modifier = Modifier
                                    .height(DimensionsCustom.shimmerTextHeight)
                                    .width(DimensionsCustom.shimmerTextWidth)
                            )
                        }

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .width(DimensionsCustom.shimmerTextWidth)
                                .padding(bottom = 16.dp)
                        )

                        ShimmerCustom(
                            modifier = Modifier
                                .clip(RoundedCornerShape(DimensionsCustom.roundedCorners))
                                .height(DimensionsCustom.tripPicHeightMax)
                                .fillMaxWidth()
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        ShimmerCustom(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DimensionsCustom.userMiniCardHeight)
                        )

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .width(DimensionsCustom.shimmerTextWidth)
                                .padding(bottom = 8.dp)
                        )

                        LazyRow {
                            items(3) {
                                ShimmerCustom(
                                    modifier = Modifier
                                        .height(DimensionsCustom.userMiniExtraCardHeight)
                                        .fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .width(DimensionsCustom.shimmerTextWidth)
                                .padding(vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(5) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(240.dp)
                            ) {
                                ShimmerCustom(
                                    modifier = Modifier
                                        .height(DimensionsCustom.userMiniExtraCardHeight)
                                        .fillMaxWidth()
                                )
                            }
                            ShimmerCustom(
                                modifier = Modifier
                                    .height(DimensionsCustom.shimmerTextHeight)
                                    .width(DimensionsCustom.shimmerTextWidth)
                                    .padding(start = 40.dp)
                            )
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun rememberViewModel(): ActualExpenseInfoViewModel = hiltViewModel()