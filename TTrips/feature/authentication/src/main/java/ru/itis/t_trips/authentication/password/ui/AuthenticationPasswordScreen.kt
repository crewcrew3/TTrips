package ru.itis.t_trips.authentication.password.ui

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
import ru.itis.t_trips.authentication.password.AuthenticationPasswordScreenEffect
import ru.itis.t_trips.authentication.password.AuthenticationPasswordScreenEvent
import ru.itis.t_trips.authentication.password.AuthenticationPasswordScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun AuthenticationPasswordScreen(
    phoneNumber: String,
) {

    val viewModel: AuthenticationPasswordViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is AuthenticationPasswordScreenEffect.Error -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = AuthenticationPasswordScreenState.Initial)
    when (pageState) {
        is AuthenticationPasswordScreenState.OnLogInSuccess -> viewModel.processEvent(AuthenticationPasswordScreenEvent.GoToTripsListScreen)
        is AuthenticationPasswordScreenState.Initial -> Unit
    }

    BaseScreen { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            Spacer(Modifier.height(DimensionsCustom.authFirstSpacerHeight))

            Text(
                text = stringResource(R.string.auth_screen_pass_header),
                style = StylesCustom.h1,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(52.dp))

            InputFieldCustom(
                startValue = formState.password,
                placeholderText = stringResource(R.string.placeholder_text_input),
                onValueChange = {
                    viewModel.processEvent(
                        AuthenticationPasswordScreenEvent.OnFormFieldChanged(
                            password = it
                        )
                    )
                                },
                isPasswordField = true,
            )

            Spacer(Modifier.height(48.dp))

            PrimaryButtonCustom(
                onBtnText = stringResource(R.string.btn_continue_text),
                onClick = {
                    viewModel.processEvent(
                        AuthenticationPasswordScreenEvent.OnLogInBtnClick(
                            phoneNumber = phoneNumber,
                            password = formState.password
                        )
                    )
                },
            )
        }
    }
}

@Composable
private fun rememberViewModel(): AuthenticationPasswordViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun AuthenticationPasswordScreenPreview() {
    TTripsTheme {
        AuthenticationPasswordScreen("")
    }
}