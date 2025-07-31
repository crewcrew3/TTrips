package ru.itis.t_trips.addactualexpense.addexpense.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.addactualexpense.addexpense.AddActualExpenseScreenEffect
import ru.itis.t_trips.addactualexpense.addexpense.AddActualExpenseScreenEvent
import ru.itis.t_trips.addactualexpense.addexpense.AddActualExpenseScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ExpenseCategoryRadioGroup
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun AddActualExpenseScreen(
    tripId: Int,
    wayToDivideAmount: String = "",
    participantsAndAmountStr: String = ""
) {
    val viewModel: AddActualExpenseViewModel = rememberViewModel()

    var isErrorDescriptionInput by remember { mutableStateOf(false) }
    var isErrorAmountInput by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is AddActualExpenseScreenEffect.Message -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
                is AddActualExpenseScreenEffect.ErrorDescriptionInput -> isErrorDescriptionInput = true
                is AddActualExpenseScreenEffect.ErrorAmountInput -> isErrorAmountInput = true
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = AddActualExpenseScreenState.Initial)
    when (pageState) {
        is  AddActualExpenseScreenState.Initial -> viewModel.processEvent(
            AddActualExpenseScreenEvent.OnScreenInit(
                tripId = tripId
            )
        )
        is AddActualExpenseScreenState.MembersResult -> Unit
    }

    val membersList = (pageState as? AddActualExpenseScreenState.MembersResult)?.members ?: emptyList()
    var membersListWithSelection by remember(membersList) { mutableStateOf(membersList) }

    var atLeastOneParticipant by remember { mutableStateOf(false) }
    var isPaidForAll by remember { mutableStateOf(false) }

    var expandedDropdown by remember { mutableStateOf(false) }

    val permissionGallery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> viewModel.processEvent(AddActualExpenseScreenEvent.OnFormFieldChanged(imageUri = uri)) }

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
            topAppBarText = stringResource(R.string.top_bar_header_add_expense),
            onBackPressed = {
                viewModel.processEvent(AddActualExpenseScreenEvent.OnBackBtnClick)
            }
        ),
    ) { innerPadding ->
        LazyColumn (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp)
        ) {
            item {
                InputFieldCustom(
                    startValue = formState.description,
                    labelText = stringResource(R.string.label_description),
                    onValueChange = {
                        viewModel.processEvent(
                            AddActualExpenseScreenEvent.OnFormFieldChanged(
                                description = it
                            )
                        )
                    },
                    isError = isErrorDescriptionInput,
                    errorText = stringResource(R.string.expense_description_regex)
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .width(320.dp)
                    ) {
                        InputFieldCustom(
                            labelText = stringResource(R.string.label_amount),
                            startValue = formState.amount,
                            onValueChange = {
                                viewModel.processEvent(AddActualExpenseScreenEvent.OnFormFieldChanged(amount = it))
                            },
                            isError = isErrorAmountInput,
                            errorText = stringResource(R.string.not_empty_regex),
                            isNumberKeyboard = true,
                        )
                    }
                    Icon(
                        imageVector = IconsCustom.rubIcon(),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(DimensionsCustom.iconSizeMid)
                            .padding(start = 8.dp)
                    )
                }

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                Text(
                    text = stringResource(R.string.label_category),
                    style = StylesCustom.body5,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

                ExpenseCategoryRadioGroup(
                    selectedCategory = formState.category,
                    onCategorySelected = {
                        viewModel.processEvent(AddActualExpenseScreenEvent.OnFormFieldChanged(category = it))
                    }
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                Text(
                    text = stringResource(R.string.label_who_you_paid),
                    style = StylesCustom.body5,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(bottom = 8.dp, start = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_you_paid_for) + " ",
                        style = StylesCustom.body3,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        Text(
                            text = stringResource(R.string.btn_chose_text),
                            style = StylesCustom.body3,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .clickable {
                                    expandedDropdown = true
                                }
                        )

                        DropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false },
                            shape = RoundedCornerShape(DimensionsCustom.roundedCorners),
                            containerColor = MaterialTheme.colorScheme.background,
                            shadowElevation = DimensionsCustom.baseElevation,
                            modifier = Modifier.width(IntrinsicSize.Max)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    UserMiniCardCustom(
                                        contact = Contact(
                                            name = stringResource(R.string.paid_for_all)
                                        ),
                                        icon = IconsCustom.peopleIcon(),
                                        isSubtitle = false,
                                        onChosenChange = { chosen ->
                                            isPaidForAll = chosen
                                            membersListWithSelection =
                                                membersListWithSelection.map {
                                                    it.copy(isChosen = false)
                                                }
                                        },
                                        cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                        avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                        iconAvatarSize = DimensionsCustom.iconSize,
                                        textStyle = StylesCustom.body3
                                    )
                                },
                                onClick = { expandedDropdown = false }
                            )
                            membersList.forEach { member ->
                                DropdownMenuItem(
                                    text = {
                                        UserMiniCardCustom(
                                            contact = member,
                                            isSubtitle = false,
                                            onChosenChange = { chosen ->
                                                membersListWithSelection =
                                                    membersListWithSelection.map {
                                                        if (it == member) it.copy(isChosen = chosen) else it
                                                    }
                                                atLeastOneParticipant = membersListWithSelection.any { it.isChosen }
                                                isPaidForAll = false
                                                viewModel.processEvent(
                                                    AddActualExpenseScreenEvent.OnFormFieldChanged(
                                                        participants = membersListWithSelection.filter { it.isChosen }
                                                    )
                                                )
                                            },
                                            cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                            avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                            iconAvatarSize = DimensionsCustom.iconSize,
                                            textStyle = StylesCustom.body3
                                        )
                                    },
                                    onClick = {}
                                )
                            }
                        }
                    }
                }

                if (!isPaidForAll) {
                    Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                    LazyRow {
                        items(formState.participants) { member ->
                            UserMiniCardCustom(
                                contact = member,
                                isSubtitle = false,
                                cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                                avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                                iconAvatarSize = DimensionsCustom.iconSize,
                                textStyle = StylesCustom.body3
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                } else {
                    Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                    UserMiniCardCustom(
                        contact = Contact(
                            name = stringResource(R.string.paid_for_all)
                        ),
                        icon = IconsCustom.peopleIcon(),
                        isSubtitle = false,
                        cardHeight = DimensionsCustom.userMiniExtraCardHeight,
                        avatarSize = DimensionsCustom.userMiniExtraCardPicSize,
                        iconAvatarSize = DimensionsCustom.iconSize,
                        textStyle = StylesCustom.body3
                    )
                }

                if (atLeastOneParticipant && formState.amount.isNotBlank()) {

                    Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                    Button(
                        shape = RoundedCornerShape(DimensionsCustom.roundedCorners),
                        onClick = {
                            viewModel.processEvent(
                                AddActualExpenseScreenEvent.OnDivideBtnClick(
                                    participants = if (!isPaidForAll) membersListWithSelection.filter { it.isChosen } else membersList,
                                    tripId = tripId,
                                    totalAmount = formState.amount.toDouble(),
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.secondary,

                        ),
                        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondary),
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier
                            .height(DimensionsCustom.btnHeight),
                    ) {
                        Text(
                            text = wayToDivideAmount.ifBlank { stringResource(R.string.btn_chose_divide_way) },
                            textAlign = TextAlign.Center,
                            style = StylesCustom.body3,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                Text(
                    text = stringResource(R.string.label_receipt),
                    style = StylesCustom.body5,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

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
                        text = if (formState.imageUri == null ) stringResource(R.string.label_add_receipt) else stringResource(R.string.label_added_receipt),
                        style = StylesCustom.body5,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                PrimaryButtonCustom(
                    onBtnText = stringResource(R.string.btn_create_text),
                    enabled = atLeastOneParticipant && participantsAndAmountStr.isNotBlank(), //нельзя создать расход, не выбрав никого, кто мог бы разделить сумму и не выбрав способ разделения суммы
                    onClick = {
                        viewModel.processEvent(
                            AddActualExpenseScreenEvent.OnSaveBtnClick(
                                tripId = tripId,
                                title = formState.description,
                                category = formState.category,
                                participantsStr = participantsAndAmountStr,
                                uri = formState.imageUri,
                                context = context
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberViewModel(): AddActualExpenseViewModel = hiltViewModel()