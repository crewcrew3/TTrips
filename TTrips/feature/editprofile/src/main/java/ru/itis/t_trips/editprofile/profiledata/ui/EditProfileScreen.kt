package ru.itis.t_trips.editprofile.profiledata.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEffect
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEvent
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenState
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.components.ImageCustom
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun EditProfileScreen(
    firstName: String,
    lastName: String,
    userPhotoUrl: String? = null
) {

    val viewModel: EditProfileViewModel = rememberViewModel()
    var isContentLoaded by remember { mutableStateOf(false) }
    if (!isContentLoaded) {
        viewModel.processEvent(
            EditProfileScreenEvent.OnFormFieldChanged(
                firstName = firstName,
                lastName = lastName,
                //uri = Uri.parse(userPhotoUrl)
            )
        )
        isContentLoaded = true
    }

    var isErrorFirstNameInput by remember { mutableStateOf(false) }
    var isErrorLastNameInput by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is EditProfileScreenEffect.Message -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
                is EditProfileScreenEffect.ErrorFirstNameInput -> isErrorFirstNameInput = true
                is EditProfileScreenEffect.ErrorLastNameInput -> isErrorLastNameInput = true
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = EditProfileScreenState.Initial)
    when (pageState) {
        is EditProfileScreenState.Initial -> Unit
    }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.processEvent(
            EditProfileScreenEvent.OnFormFieldChanged(
                uri = uri
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
            topAppBarText = stringResource(R.string.top_bar_header_edit_profile),
            onBackPressed = {
                viewModel.processEvent(
                    EditProfileScreenEvent.OnBackBtnClick
                )
            },
        )
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp)
        ) {
            item {
                // Кружок с иконкой
                Box(
                    modifier = Modifier
                        .clickable {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    permission
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                pickImageLauncher.launch("image/*")
                            } else {
                                permissionLauncher.launch(permission)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (formState.uri != null) {
                        ImageCustom(
                            url = formState.uri.toString(),
                            imageShape = CircleShape,
                            modifier = Modifier
                                .size(184.dp)
                        )
                    } else {
                        ImageCustom(
                            imageShape = CircleShape,
                            modifier = Modifier
                                .size(184.dp)
                        )
                        Icon(
                            imageVector = IconsCustom.addPhotoIcon(),
                            contentDescription = "",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                InputFieldCustom(
                    startValue = formState.firstName,
                    labelText = stringResource(R.string.label_name),
                    onValueChange = {
                         EditProfileScreenEvent.OnFormFieldChanged(
                             firstName = it
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
                            EditProfileScreenEvent.OnFormFieldChanged(
                                lastName = it
                            )
                        )
                    },
                    isError = isErrorLastNameInput,
                    errorText = stringResource(R.string.last_name_regex),
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                PrimaryButtonCustom(
                    onBtnText = stringResource(R.string.btn_save_text),
                    onClick = {
                        viewModel.processEvent(
                            EditProfileScreenEvent.OnSaveBtnClick(
                                firstName = formState.firstName,
                                lastName = formState.lastName,
                                uri = formState.uri
                            ),
                            context = context
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberViewModel(): EditProfileViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun EditProfileScreenPreview() {
    TTripsTheme {
        EditProfileScreen("Vasya", "Pupkin")
    }
}