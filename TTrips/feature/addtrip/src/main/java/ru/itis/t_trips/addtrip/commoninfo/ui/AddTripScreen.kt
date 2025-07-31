package ru.itis.t_trips.addtrip.commoninfo.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.addtrip.commoninfo.AddTripScreenEffect
import ru.itis.t_trips.addtrip.commoninfo.AddTripScreenEvent
import ru.itis.t_trips.addtrip.commoninfo.AddTripScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ImageCustom
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.TTripsTheme
import java.time.format.DateTimeFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import androidx.compose.foundation.lazy.items
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.IconSettings
import ru.itis.t_trips.ui.model.Contact
import java.time.Instant
import java.time.ZoneId

@Composable
fun AddTripScreen(
    contactsStr: String = "",
) {

    val viewModel: AddTripViewModel = rememberViewModel()

    var isErrorTitleInput by remember { mutableStateOf(false) }
    var isErrorBudgetInput by remember { mutableStateOf(false) }

    val isContactsLoaded = remember { mutableStateOf(false) }
    if (!isContactsLoaded.value && contactsStr.isNotBlank()) {
        viewModel.processEvent(
            AddTripScreenEvent.OnAddMembers(
                contacts = contactsStr
            )
        )
        isContactsLoaded.value = true
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is AddTripScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
                is AddTripScreenEffect.ErrorTripTitleInput -> isErrorTitleInput = true
                is AddTripScreenEffect.ErrorTripBudgetInput -> isErrorBudgetInput = true
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateFormatterUi = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    LaunchedEffect(isPressed) {
        if (isPressed) {
            showDatePicker = true
        }
    }

    var membersList by remember { mutableStateOf<List<Contact>>(emptyList()) }
    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = AddTripScreenState.Initial)
    when (pageState) {
        is AddTripScreenState.Result -> membersList = (pageState as AddTripScreenState.Result).contacts
        is AddTripScreenState.Initial -> Unit
    }

    // начиная с 13 андроида используется READ_MEDIA_IMAGES
    val permissionGallery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val permissionContacts = android.Manifest.permission.READ_CONTACTS

    // Лаунчер для выбора изображения из галереи
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.processEvent(
            AddTripScreenEvent.OnFormFieldChanged(uri = uri)
        )
    }

    // Лаунчер для разрешений
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val galleryGranted = permissions[permissionGallery] == true
        val contactsGranted = permissions[permissionContacts] == true

        if (galleryGranted) {
            pickImageLauncher.launch("image/*")
        }

        if (contactsGranted) {
            viewModel.processEvent(
                AddTripScreenEvent.OnAddMembersBtnClick
            )
        }
    }


    BaseScreen(
        isTopBar = true,
        isFloatingActionButton = true,
        floatingActBtnSettings = FloatingActionButtonSettings.RectangleButtonSettings(
            onBtnText = stringResource(R.string.btn_create_text),
            onClick = {
                viewModel.processEvent(
                    AddTripScreenEvent.OnSaveBtnClick(
                        title = formState.title,
                        startDate = formState.startDate,
                        endDate = formState.endDate,
                        budget = formState.budget,
                        uri = formState.uri,
                        membersList = membersList,
                    ),
                    context = context
                )
            },
        ),
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_add_trip),
            onBackPressed = {
                viewModel.processEvent(
                    AddTripScreenEvent.OnBackBtnClick
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Box(
                            modifier = Modifier
                                .clickable {
                                    if (ContextCompat.checkSelfPermission(context, permissionGallery) == PackageManager.PERMISSION_GRANTED) {
                                        pickImageLauncher.launch("image/*")
                                    } else {
                                        permissionLauncher.launch(arrayOf(permissionGallery))
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (formState.uri != null) {
                                ImageCustom(
                                    url = formState.uri.toString(),
                                    modifier = Modifier
                                        .height(DimensionsCustom.tripPicHeight)
                                        .width(DimensionsCustom.tripPicWidthMini)
                                )
                            } else {
                                ImageCustom(
                                    modifier = Modifier
                                        .height(DimensionsCustom.tripPicHeight)
                                        .width(DimensionsCustom.tripPicWidthMini)
                                )
                                Icon(
                                    imageVector = IconsCustom.addPhotoIcon(),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        InputFieldCustom(
                            startValue = formState.title,
                            labelText = stringResource(R.string.label_title),
                            onValueChange = {
                                viewModel.processEvent(
                                    AddTripScreenEvent.OnFormFieldChanged(
                                        title = it
                                    )
                                )
                            },
                            isError = isErrorTitleInput,
                            errorText = stringResource(R.string.trip_title_regex),
                            modifier = Modifier
                                .height(52.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(DimensionsCustom.spaceInputFields))
            }

            item {
                Box(
                    modifier = Modifier
                ) {
                    //календарь
                    OutlinedTextField(
                        value = if (formState.startDateUi.isNotBlank() && formState.endDateUi.isNotBlank()) {
                            "${formState.startDateUi} - ${formState.endDateUi}"
                        } else {
                            ""
                        },
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.label_choose_date)) },
                        readOnly = true,
                        interactionSource = interactionSource,
                        trailingIcon = {
                            Icon(
                                imageVector = IconsCustom.calendarIcon(),
                                contentDescription = stringResource(R.string.label_choose_date),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(DimensionsCustom.roundedCorners),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    )
                }

                Spacer(modifier = Modifier.height(DimensionsCustom.spaceInputFields))
            }

            item {
                InputFieldCustom(
                    startValue = formState.budget,
                    labelText = stringResource(R.string.label_budget),
                    onValueChange = {
                        viewModel.processEvent(
                            AddTripScreenEvent.OnFormFieldChanged(
                                budget = it
                            )
                        )
                    },
                    isError = isErrorBudgetInput,
                    errorText = stringResource(R.string.not_empty_regex),
                    isNumberKeyboard = true,
                )

                Spacer(modifier = Modifier.height(DimensionsCustom.spaceInputFields))

                PrimaryButtonCustom(
                    onBtnText = stringResource(R.string.btn_add_members_text),
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, permissionContacts) == PackageManager.PERMISSION_GRANTED) {
                            viewModel.processEvent(
                                AddTripScreenEvent.OnAddMembersBtnClick
                            )
                        } else {
                            permissionLauncher.launch(arrayOf(permissionContacts))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(DimensionsCustom.spaceInputFields))
            }

            items(membersList) { contact ->
                UserMiniCardCustom(
                    contact = contact,
                    isIcon = true,
                    iconSettings = IconSettings(
                        icon = IconsCustom.crossIcon(),
                        iconTint = MaterialTheme.colorScheme.onTertiary,
                        onClick = {
                            viewModel.processEvent(
                                AddTripScreenEvent.OnRemoveContact(contact)
                            )
                        }
                    )
                )
            }
        }
        if (showDatePicker) {
            DateRangePickerModal(
                onDateRangeSelected = { range ->
                    val (startMillis, endMillis) = range
                    if (startMillis != null && endMillis != null) {
                        val startDate = Instant.ofEpochMilli(startMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        val endDate = Instant.ofEpochMilli(endMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.processEvent(
                            AddTripScreenEvent.OnFormFieldChanged(
                                startDateUi = startDate.format(dateFormatterUi),
                                endDateUi = endDate.format(dateFormatterUi),
                                startDate = startDate.format(dateFormatter),
                                endDate = endDate.format(dateFormatter)
                            )
                        )
                    } else {
                        AddTripScreenEvent.OnFormFieldChanged(
                            startDateUi = "",
                            endDateUi = "",
                            startDate = "",
                            endDate = ""
                        )
                    }
                    showDatePicker = false
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(R.string.btn_ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(R.string.btn_cancel))
            }
        },
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = stringResource(R.string.title_calendar)
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(376.dp)
                .padding(16.dp)
        )
    }
}

@Composable
private fun rememberViewModel(): AddTripViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun AddTripScreenPreview() {
    TTripsTheme {
        AddTripScreen()
    }
}