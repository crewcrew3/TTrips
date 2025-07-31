package ru.itis.t_trips.editprofile.privacy.ui

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.editprofile.privacy.PrivacyScreenEffect
import ru.itis.t_trips.editprofile.privacy.PrivacyScreenEvent
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEvent
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun PrivacyScreen(
    phoneNumber: String,
){

    val viewModel: PrivacyViewModel = rememberViewModel()
    var isContentLoaded by remember { mutableStateOf(false) }
    if (!isContentLoaded) {
        viewModel.processEvent(
            PrivacyScreenEvent.OnFormFieldChanged(
                phoneNumber = phoneNumber
            )
        )
        isContentLoaded = true
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is PrivacyScreenEffect.Message -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
                is PrivacyScreenEffect.ErrorInput -> Unit
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = EditProfileScreenState.Initial)
    when (pageState) {
        is EditProfileScreenState.Initial -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.profile_screen_tab_privacy),
            onBackPressed = {
                viewModel.processEvent(
                    PrivacyScreenEvent.OnBackBtnClick
                )
            },
        )
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp)
        ) {

            Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

            InputFieldCustom(
                startValue = formState.phoneNumber,
                labelText = stringResource(R.string.label_phone_number),
                onValueChange = {
                    viewModel.processEvent(
                        PrivacyScreenEvent.OnFormFieldChanged(
                            phoneNumber = it
                        )
                    )
                }
            )

            Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

            InputFieldCustom(
                startValue = formState.password,
                labelText = stringResource(R.string.label_pass),
                isPasswordField = true,
                onValueChange = {
                    viewModel.processEvent(
                        PrivacyScreenEvent.OnFormFieldChanged(
                            password = it
                        )
                    )
                }
            )

            Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

            PrimaryButtonCustom(
                onBtnText = stringResource(R.string.btn_save_text),
                onClick = {
                    viewModel.processEvent(
                        PrivacyScreenEvent.OnSaveBtnClick(
                            phoneNumber = formState.phoneNumber,
                            password = formState.password,
                        )
                    )
                }
            )

        }
    }
}

@Composable
private fun rememberViewModel(): PrivacyViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun PrivacyScreenPreview() {
    TTripsTheme {
        PrivacyScreen("88005555535")
    }
}