package ru.itis.t_trips.authentication.phonenumber.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenEffect
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenEvent
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun AuthenticationPhoneNumberScreen() {

    val viewModel: AuthenticationPhoneNumberViewModel = rememberViewModel()

    var isErrorInput by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is AuthenticationPhoneNumberScreenEffect.Error -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
                is AuthenticationPhoneNumberScreenEffect.ErrorInput -> isErrorInput = true
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = AuthenticationPhoneNumberScreenState.Initial)
    when (pageState) {
        is AuthenticationPhoneNumberScreenState.Initial -> Unit
    }

    BaseScreen { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            Spacer(Modifier.height(DimensionsCustom.authFirstSpacerHeight))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.auth_screen_phone_num_header),
                    style = StylesCustom.h1,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.auth_screen_phone_num_text),
                    style = StylesCustom.body3,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.phone_num_regex),
                    style = StylesCustom.body2,
                    color = MaterialTheme.colorScheme.onTertiary
                )

            }

            Spacer(Modifier.height(72.dp))

            InputFieldCustom(
                startValue = formState.phoneNumber,
                placeholderText = stringResource(R.string.placeholder_text_input),
                onValueChange = {
                    viewModel.processEvent(
                        AuthenticationPhoneNumberScreenEvent.OnFormFieldChanged(
                            phoneNumber = it
                        )
                    )
                },
                isError = isErrorInput,
                errorText = stringResource(R.string.phone_num_regex)
            )

            Spacer(Modifier.height(48.dp))

            PrimaryButtonCustom(
                onBtnText = stringResource(R.string.btn_continue_text),
                onClick = {
                    viewModel.processEvent(
                        AuthenticationPhoneNumberScreenEvent.OnContinueBtnClick(
                            phoneNumber = formState.phoneNumber
                        )
                    )
                },
            )
        }
    }
}

@Composable
private fun rememberViewModel(): AuthenticationPhoneNumberViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun AuthenticationPhoneNumberScreenPreview() {
    TTripsTheme {
        AuthenticationPhoneNumberScreen()
    }
}