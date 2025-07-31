package ru.itis.t_trips.addplannedexpense.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.addplannedexpense.AddPlannedExpenseScreenEffect
import ru.itis.t_trips.addplannedexpense.AddPlannedExpenseScreenEvent
import ru.itis.t_trips.addplannedexpense.AddPlannedExpenseScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.ExpenseCategoryRadioGroup
import ru.itis.t_trips.ui.components.InputFieldCustom
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun AddPlannedExpenseScreen(
    tripId: Int,
) {

    val viewModel: AddPlannedExpenseViewModel = rememberViewModel()

    var isErrorDescriptionInput by remember { mutableStateOf(false) }
    var isErrorAmountInput by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is AddPlannedExpenseScreenEffect.Message -> Toast.makeText(context, context.getText(effect.message), Toast.LENGTH_SHORT).show()
                is AddPlannedExpenseScreenEffect.ErrorDescriptionInput -> isErrorDescriptionInput = true
                is AddPlannedExpenseScreenEffect.ErrorAmountInput -> isErrorAmountInput = true
            }
        }
    }

    val formState by viewModel.formState.collectAsState()
    val pageState by viewModel.pageState.collectAsState(initial = AddPlannedExpenseScreenState.Initial)
    when (pageState) {
        is  AddPlannedExpenseScreenState.Initial -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_add_expense),
            onBackPressed = {
                viewModel.processEvent(AddPlannedExpenseScreenEvent.OnBackBtnClick)
            }
        ),
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp)
        ) {
            item {
                InputFieldCustom(
                    startValue = formState.title,
                    labelText = stringResource(R.string.label_description),
                    onValueChange = {
                        viewModel.processEvent(
                            AddPlannedExpenseScreenEvent.OnFormFieldChanged(
                                title = it
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
                                viewModel.processEvent(
                                    AddPlannedExpenseScreenEvent.OnFormFieldChanged(
                                        amount = it
                                    )
                                )
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
                        viewModel.processEvent(
                            AddPlannedExpenseScreenEvent.OnFormFieldChanged(
                                category = it
                            )
                        )
                    }
                )

                Spacer(Modifier.height(DimensionsCustom.spaceInputFields))

                PrimaryButtonCustom(
                    onBtnText = stringResource(R.string.btn_create_text),
                    onClick = {
                        viewModel.processEvent(
                            AddPlannedExpenseScreenEvent.OnSaveBtnClick(
                                tripId = tripId,
                                title = formState.title,
                                category = formState.category,
                                amount = formState.amount
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberViewModel(): AddPlannedExpenseViewModel = hiltViewModel()