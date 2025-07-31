package ru.itis.t_trips.authentication.registration.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import ru.itis.t_trips.authentication.registration.RegistrationScreenEffect
import ru.itis.t_trips.authentication.registration.RegistrationScreenEvent
import ru.itis.t_trips.authentication.registration.RegistrationScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun RegistrationScreen(
    phoneNumber: String,
) {

    val viewModel: RegistrationViewModel = rememberViewModel()

    var isErrorFirstNameInput by remember { mutableStateOf(false) }
    var isErrorLastNameInput by remember { mutableStateOf(false) }
    var isErrorPasswordInput by remember { mutableStateOf(false) }
    var isErrorRepeatPasswordInput by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is RegistrationScreenEffect.Error -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
                is RegistrationScreenEffect.ErrorFirstNameInput -> isErrorFirstNameInput = true
                is RegistrationScreenEffect.ErrorLastNameInput -> isErrorLastNameInput = true
                is RegistrationScreenEffect.ErrorPasswordInput -> isErrorPasswordInput = true
                is RegistrationScreenEffect.ErrorRepeatPasswordInput -> isErrorRepeatPasswordInput = true
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = RegistrationScreenState.Initial)
    when (pageState) {
        is RegistrationScreenState.OnSignUpSuccess -> viewModel.processEvent(RegistrationScreenEvent.GoToTripsListScreen)
        is RegistrationScreenState.Initial -> Unit
    }


    BaseScreen { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                Spacer(Modifier.height(DimensionsCustom.authFirstSpacerHeight))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.registration_screen_header),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = StylesCustom.h1,
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.registration_screen_text),
                        style = StylesCustom.body3,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }


                Spacer(Modifier.height(56.dp))

                InputFieldCustom(
                    startValue = formState.firstName,
                    labelText = stringResource(R.string.label_name),
                    onValueChange = {
                        viewModel.processEvent(
                            RegistrationScreenEvent.OnFormFieldChanged(
                                firstName = it
                            )
                        )
                    },
                    isError = isErrorFirstNameInput,
                    errorText = stringResource(R.string.first_name_regex),
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                InputFieldCustom(
                    startValue = formState.lastName,
                    labelText = stringResource(R.string.label_surname),
                    onValueChange = {
                        viewModel.processEvent(
                            RegistrationScreenEvent.OnFormFieldChanged(
                                lastName = it
                            )
                        )
                    },
                    isError = isErrorLastNameInput,
                    errorText = stringResource(R.string.last_name_regex),
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                InputFieldCustom(
                    startValue = formState.password,
                    labelText = stringResource(R.string.label_pass),
                    onValueChange = {
                        viewModel.processEvent(
                            RegistrationScreenEvent.OnFormFieldChanged(
                                password = it
                            )
                        )
                    },
                    isError = isErrorPasswordInput,
                    errorText = stringResource(R.string.password_name_regex),
                    isPasswordField = true
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                InputFieldCustom(
                    startValue = formState.repeatPassword,
                    labelText = stringResource(R.string.label_repeat_pass),
                    onValueChange = {
                        viewModel.processEvent(
                            RegistrationScreenEvent.OnFormFieldChanged(
                                repeatPassword = it
                            )
                        )
                    },
                    isError = isErrorRepeatPasswordInput,
                    errorText = stringResource(R.string.repeat_password_error),
                    isPasswordField = true,
                )

                Spacer(Modifier.height(48.dp))

                PrimaryButtonCustom(
                    onBtnText = stringResource(R.string.btn_save_text),
                    onClick = {
                        viewModel.processEvent(
                            RegistrationScreenEvent.OnSignUpBtnClick(
                                firstName = formState.firstName,
                                lastName = formState.lastName,
                                phoneNumber = phoneNumber,
                                password = formState.password,
                                repeatPassword = formState.repeatPassword
                            )
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun rememberViewModel(): RegistrationViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun RegistrationScreenPreview() {
    TTripsTheme {
        RegistrationScreen("")
    }
}