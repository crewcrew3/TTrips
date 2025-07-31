package ru.itis.profile.ui

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.profile.ProfileScreenEffect
import ru.itis.profile.ProfileScreenEvent
import ru.itis.profile.ProfileScreenState
import ru.itis.t_trips.domain.model.UserProfileModel
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.DividerCustom
import ru.itis.t_trips.ui.components.ImageCustom
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.theme.ColorsCustom
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun ProfileScreen(
    //previewModel: UserProfileModel? = null,
) {

    val viewModel: ProfileViewModel = rememberViewModel()

    val context = LocalContext.current
    val activity = context as? Activity
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is ProfileScreenEffect.Error -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    var phoneNumber by remember { mutableStateOf("") }

    val pageState by viewModel.pageState.collectAsState(initial = ProfileScreenState.Initial)
    when (pageState) {
        is ProfileScreenState.Initial -> viewModel.processEvent(ProfileScreenEvent.OnInitProfile)
        is ProfileScreenState.UserProfileResult, ProfileScreenState.Loading -> Unit
    }

    val currentLang by viewModel.currentLocale.collectAsState()

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
                    text = stringResource(R.string.title_profile),
                    style = StylesCustom.h5,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 24.dp)
                )
            }
            when(pageState) {
                is ProfileScreenState.UserProfileResult -> {
                    val profileModel = (pageState as ProfileScreenState.UserProfileResult).result
                    val userPhotoUrl = (pageState as ProfileScreenState.UserProfileResult).photoUrl
                    phoneNumber = profileModel.phoneNumber
                    item {
                        ProfileCard(
                            userProfileModel = profileModel,
                            userPhotoUrl = userPhotoUrl,
                            onIconClick = {
                                viewModel.processEvent(
                                    ProfileScreenEvent.OnEditProfileBtnClick(
                                        firstName = profileModel.firstName,
                                        lastName = profileModel.lastName,
                                        userPhotoUrl = userPhotoUrl
                                    )
                                )
                            }
                        )
                    }
                    item {
                        DividerCustom()
                        ProfileTab(
                            tabIcon = IconsCustom.tabArchiveIcon(),
                            tabHeader = stringResource(R.string.profile_screen_tab_archive),
                            onClick = {
                                viewModel.processEvent(
                                    ProfileScreenEvent.OnTripArchiveTabClick
                                )
                            },
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        DividerCustom()
                        Text(
                            text = stringResource(R.string.profile_screen_settings_header),
                            style = StylesCustom.body4,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(bottom = 4.dp)
                        )

                        ProfileTab(
                            tabIcon = IconsCustom.languageIcon(),
                            tabHeader = stringResource(R.string.profile_screen_tab_change_locale),
                            onClick = {
                                showDialog = true
                            }
                        )

                        if (showDialog) {
                            LanguageDialog(
                                currentLang = currentLang,
                                onDismiss = { showDialog = false },
                                onDone = { lang ->
                                    viewModel.processEvent(
                                        ProfileScreenEvent.OnChangeLocale(
                                            locale = lang
                                        )
                                    )
                                    showDialog = false
                                    activity?.recreate()
                                }
                            )
                        }

                        ProfileTab(
                            tabIcon = IconsCustom.tabPrivacyIcon(),
                            tabHeader = stringResource(R.string.profile_screen_tab_privacy),
                            onClick = {
                                viewModel.processEvent(
                                    ProfileScreenEvent.OnPrivacyTabClick(
                                        phoneNumber = phoneNumber
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        DividerCustom()

                        ProfileTab(
                            tabIcon = IconsCustom.tabLogOutIcon(),
                            tabHeader = stringResource(R.string.profile_screen_tab_logout),
                            onClick = {
                                viewModel.processEvent(
                                    ProfileScreenEvent.OnLogOutTabClick
                                )
                            },
                            textColor = ColorsCustom.JustRedColor,
                            iconColor = ColorsCustom.JustRedColor,
                        )
                    }
                }

                is ProfileScreenState.Loading -> {
                    item {
                        ShimmerCustom(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(DimensionsCustom.profileCardHeight),
                        )
                    }

                    item {
                        DividerCustom()
                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .fillMaxWidth()
                                .padding(top = 28.dp, bottom = 28.dp)
                        )
                        DividerCustom()
                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .fillParentMaxWidth()
                                .padding(bottom = 4.dp)
                        )

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .fillMaxWidth()
                                .padding(top = 28.dp, bottom = 28.dp)
                        )
                        DividerCustom()

                        ShimmerCustom(
                            modifier = Modifier
                                .height(DimensionsCustom.shimmerTextHeight)
                                .fillMaxWidth()
                                .padding(top = 28.dp, bottom = 28.dp)
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun ProfileCard(
    userProfileModel: UserProfileModel,
    userPhotoUrl: String?,
    onIconClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimensionsCustom.profileCardHeight),
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
                contentAlignment = Alignment.Center
            ) {
                if (userPhotoUrl != null) {
                    ImageCustom(
                        url = userPhotoUrl,
                        imageShape = CircleShape,
                        modifier = Modifier
                            .height(DimensionsCustom.profilePicSize)
                            .width(DimensionsCustom.profilePicSize)
                    )
                } else {
                    ImageCustom(
                        imageShape = CircleShape,
                        modifier = Modifier
                            .height(DimensionsCustom.profilePicSize)
                            .width(DimensionsCustom.profilePicSize)
                    )
                    Icon(
                        imageVector = IconsCustom.profileIcon(),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                        modifier = Modifier
                            .height(DimensionsCustom.iconSizeMaxi)
                            .width(DimensionsCustom.iconSizeMaxi)
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
                        text = userProfileModel.firstName + " " + userProfileModel.lastName,
                        style = StylesCustom.h2,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 16.dp, start = 20.dp)
                    )

                    Text(
                        text = "+" + userProfileModel.phoneNumber,
                        style = StylesCustom.body3,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(top = 8.dp, start = 20.dp)
                        )
                }
                Icon(
                    imageVector = IconsCustom.editIcon(),
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(DimensionsCustom.iconSize)
                        .width(DimensionsCustom.iconSize)
                        .clickable { onIconClick() }
                )
            }
        }
    }
}

@Composable
fun ProfileTab(
    tabIcon: ImageVector,
    tabHeader: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    iconColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 8.dp)
            .clickable { onClick() },
    ) {
        Icon(
            imageVector = tabIcon,
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier
                .padding(start = 20.dp)
                .height(DimensionsCustom.iconSize)
                .width(DimensionsCustom.iconSize)
        )
        Text(
            text = tabHeader,
            style = StylesCustom.h6,
            color = textColor,
            modifier = Modifier
                .padding(start = 24.dp)
        )
    }
}

@Composable
fun LanguageDialog(
    currentLang: String,
    onDismiss: () -> Unit,
    onDone: (String) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(currentLang) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.profile_screen_tab_change_locale),
                style = StylesCustom.h11,
                color = MaterialTheme.colorScheme.onPrimary
            )
                },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable { selectedLanguage = "ru" }
                ) {
                    RadioButton(
                        selected = selectedLanguage == "ru",
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                    Text(
                        text = stringResource(R.string.dialog_language_option_ru),
                        style = StylesCustom.body3,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedLanguage = "en" }
                ) {
                    RadioButton(
                        selected = selectedLanguage == "en",
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                    Text(
                        text = stringResource(R.string.dialog_language_option_en),
                        style = StylesCustom.body3,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onDone(selectedLanguage)
                }
            ) {
                Text(
                    text = stringResource(R.string.dialog_language_btn_done),
                    style = StylesCustom.body3,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.dialog_language_btn_cancel),
                    style = StylesCustom.body3,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    )
}



@Composable
private fun rememberViewModel(): ProfileViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun ProfileScreenPreview() {
    TTripsTheme {
        ProfileScreen(
//            previewModel = UserProfileModel(
//                id = 1,
//                name = "Вася Пупкин",
//                phoneNumber = "88005555535"
//            )
        )
    }
}