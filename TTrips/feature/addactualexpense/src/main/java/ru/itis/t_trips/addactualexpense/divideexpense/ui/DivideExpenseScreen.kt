package ru.itis.t_trips.addactualexpense.divideexpense.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.json.Json
import ru.itis.t_trips.addactualexpense.divideexpense.DivideExpenseScreenEffect
import ru.itis.t_trips.addactualexpense.divideexpense.DivideExpenseScreenEvent
import ru.itis.t_trips.addactualexpense.divideexpense.DivideExpenseScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.DividerCustom
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.components.settings.IconSettings
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.ui.model.enums.DivideExpenseTab
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun DivideExpenseScreen(
    tripId: Int,
    participantsStr: String,
    totalAmount: Double,
) {

    val viewModel: DivideExpenseViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is DivideExpenseScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val participantsList: List<Contact> = Json.decodeFromString(participantsStr) ?: emptyList()

    val pageState by viewModel.pageState.collectAsState(initial = DivideExpenseScreenState.Initial)
    when (pageState) {
        is DivideExpenseScreenState.Initial -> Unit
    }

    var participantsMap by remember { mutableStateOf<Map<Int, Double>>(emptyMap()) }
    var selectedTab by remember { mutableStateOf(DivideExpenseTab.EQUALLY) }
    val wayToDivideAmount = stringResource(selectedTab.stringResourceId)

    BaseScreen(
        isTopBar = true,
        isFloatingActionButton = participantsMap.isNotEmpty() && (participantsMap.values.sumOf { it } == totalAmount),
        floatingActBtnSettings = FloatingActionButtonSettings.CircleButtonSettings(
            icon = IconsCustom.arrowForwardIcon(),
            onClick = {
                viewModel.processEvent(
                    DivideExpenseScreenEvent.OnNextBtnClick(
                        tripId = tripId,
                        wayToDivideAmount = wayToDivideAmount,
                        participants = participantsMap
                    ),
                )
            },
        ),
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_divide_expense),
            onBackPressed = {
                viewModel.processEvent(
                    DivideExpenseScreenEvent.OnBackBtnClick
                )
            },
        )
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 24.dp)
                .fillMaxSize()
        ) {

            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                indicator = { tabPositions ->
                    val selectedTabPosition = tabPositions[selectedTab.ordinal]
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(selectedTabPosition)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .height(4.dp)
                    )
                }
            ) {
                DivideExpenseTab.entries.forEach { tab ->
                    Tab(
                        selected = tab == selectedTab,
                        selectedContentColor = MaterialTheme.colorScheme.secondary,
                        unselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            selectedTab = tab
                            participantsMap = emptyMap()
                        },
                        text = {
                            Text(
                                text = when (tab) {
                                    DivideExpenseTab.EQUALLY -> stringResource(R.string.tab_text_divide_expense_equally)
                                    DivideExpenseTab.NOTEQUALLY -> stringResource(R.string.tab_text_divide_expense_not_equally)
                                },
                                style = StylesCustom.body3,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            }

            if (selectedTab == DivideExpenseTab.EQUALLY) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(participantsList) { participant ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .width(320.dp)
                            ) {
                                UserMiniCardCustom(
                                    contact = participant,
                                    isSubtitle = false,
                                )
                            }

                            var checked by remember { mutableStateOf(false) }

                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    checked = isChecked
                                    participantsMap = if (checked) {
                                        participantsMap + (participant.id to totalAmount / participantsList.size)
                                    } else {
                                        participantsMap - participant.id
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.secondary,
                                    checkmarkColor = MaterialTheme.colorScheme.background,
                                )
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.price_per_person, (totalAmount / participantsList.size)),
                    style = StylesCustom.body5,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = stringResource(R.string.price_persons_num, participantsMap.size),
                    style = StylesCustom.body3,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            } else if (selectedTab == DivideExpenseTab.NOTEQUALLY) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(participantsList) { participant ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .width(320.dp)
                            ) {
                                UserMiniCardCustom(
                                    contact = participant,
                                    isSubtitle = false,
                                )
                            }

                            var textState by remember { mutableStateOf("") }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .width(40.dp)
                                ) {
                                    BasicTextField(
                                        value = textState,
                                        onValueChange = { input ->
                                            val filteredInput = input.filter { it.isDigit() }
                                            textState = filteredInput
                                            if (textState.isNotBlank()) participantsMap = participantsMap + (participant.id to textState.toDouble())
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        textStyle = StylesCustom.body3.copy(textAlign = TextAlign.End),
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true
                                    )
                                    DividerCustom()
                                }

                                Icon(
                                    imageVector = IconsCustom.rubIcon(),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .size(DimensionsCustom.iconSize)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.price_div, participantsMap.values.sumOf { it }, totalAmount),
                    style = StylesCustom.body5,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = stringResource(R.string.price_left, (totalAmount - participantsMap.values.sumOf { it })),
                    style = StylesCustom.body3,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }
}

@Composable
private fun rememberViewModel(): DivideExpenseViewModel = hiltViewModel()