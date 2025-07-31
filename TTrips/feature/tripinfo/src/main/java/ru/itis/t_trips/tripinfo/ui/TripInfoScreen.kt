package ru.itis.t_trips.tripinfo.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.t_trips.domain.model.expense.ExpenseModel
import ru.itis.t_trips.domain.model.expense.PlannedExpenseModel
import ru.itis.t_trips.ui.model.enums.ExpenseTab
import ru.itis.t_trips.tripinfo.TripInfoScreenEffect
import ru.itis.t_trips.tripinfo.TripInfoScreenEvent
import ru.itis.t_trips.tripinfo.TripInfoScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.ui.components.PrimaryButtonCustom
import ru.itis.t_trips.ui.components.ShimmerCustom
import ru.itis.t_trips.ui.components.TripListItem
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.components.settings.IconSettings
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.components.settings.TripListItemSettings
import ru.itis.t_trips.ui.model.enums.ExpenseCategory
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme
import kotlin.math.exp

@Composable
fun TripInfoScreen(
    tripId: Int,
) {

    val viewModel: TripInfoViewModel = rememberViewModel()

    var isCreator by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(ExpenseTab.PLANNED) }
    var selectedCategoryTab by remember { mutableStateOf(ExpenseCategory.FLIGHT) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is TripInfoScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = TripInfoScreenState.Initial)
    when (pageState) {
        is TripInfoScreenState.Initial -> viewModel.processEvent(
            TripInfoScreenEvent.OnInitScreen(
                tripId = tripId
            )
        )

        is TripInfoScreenState.TripInfoResult, TripInfoScreenState.Loading -> Unit
    }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_look_trip_info),
            onBackPressed = {
                viewModel.processEvent(TripInfoScreenEvent.OnBackBtnClick)
            }
        ),
        isFloatingActionButton = true,
        floatingActBtnSettings = FloatingActionButtonSettings.CircleButtonSettings(
            icon = IconsCustom.addContentIcon(),
            onClick = {
                if (selectedTab == ExpenseTab.ACTUAL) {
                    viewModel.processEvent(
                        TripInfoScreenEvent.OnAddActualExpense(tripId)
                    )
                } else {
                    viewModel.processEvent(
                        TripInfoScreenEvent.OnAddPlannedExpense(tripId)
                    )
                }
            }
        )
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when (pageState) {
                is TripInfoScreenState.TripInfoResult -> {
                    val resultState = (pageState as TripInfoScreenState.TripInfoResult)
                    val tripItem = resultState.result
                    val tripPicUrl = resultState.picUrl

                    isCreator = resultState.isCreator
                    selectedTab = resultState.selectedTab
                    selectedCategoryTab = resultState.selectedCategoryTab

                    val plannedExpenses = resultState.plannedExpenses
                    val actualExpenses = resultState.actualExpenses

                    val plannedExpensesFiltered =
                        plannedExpenses.filter { item -> item.category == selectedCategoryTab.toString() }
                    val actualExpensesFiltered =
                        actualExpenses.filter { item -> item.category == selectedCategoryTab.toString() }

                    TripListItem(
                        itemSettings = TripListItemSettings(
                            id = tripItem.id,
                            title = tripItem.title,
                            status = tripItem.status,
                            startDate = tripItem.startDate,
                            endDate = tripItem.endDate,
                            picUrl = tripPicUrl
                        ),
                        isIcon = isCreator,
                        iconSettings = IconSettings(
                            icon = IconsCustom.editIcon(),
                            iconTint = MaterialTheme.colorScheme.secondary,
                            onClick = {
                                viewModel.processEvent(
                                    TripInfoScreenEvent.OnEditBtnClick(
                                        tripId = tripItem.id
                                    )
                                )
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PrimaryButtonCustom(
                        onBtnText = stringResource(R.string.btn_look_members_text),
                        btnHeight = 44.dp,
                        textStyle = StylesCustom.h6,
                        onClick = {
                            viewModel.processEvent(
                                TripInfoScreenEvent.OnLookMembersBtnClick(
                                    tripId = tripId
                                )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TabRow(
                        selectedTabIndex = selectedTab.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        indicator = { tabPositions ->
                            // Получаем позицию выбранной вкладки
                            val selectedTabPosition = tabPositions[selectedTab.ordinal]
                            // Рисуем индикатор
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(selectedTabPosition) // Устанавливаем позицию индикатора
                                    .background(MaterialTheme.colorScheme.outlineVariant) // Устанавливаем цвет индикатора
                                    .height(4.dp) // Устанавливаем высоту индикатора
                            )
                        }
                    ) {
                        ExpenseTab.entries.forEach { tab ->
                            Tab(
                                selected = tab == selectedTab,
                                selectedContentColor = MaterialTheme.colorScheme.secondary,
                                unselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                onClick = {
                                    viewModel.processEvent(TripInfoScreenEvent.OnTabSelected(tab = tab))
                                },
                                text = {
                                    Text(
                                        text = when (tab) {
                                            ExpenseTab.PLANNED -> stringResource(R.string.tab_text_planned_expenses)
                                            ExpenseTab.ACTUAL -> stringResource(R.string.tab_text_actual_expenses)
                                        },
                                        style = StylesCustom.body4Light,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    }

                    val currentListFiltered =
                        if (selectedTab == ExpenseTab.ACTUAL) actualExpensesFiltered else plannedExpensesFiltered
                    val currentList =
                        if (selectedTab == ExpenseTab.ACTUAL) actualExpenses else plannedExpenses

                    val expenseTotalAmount = currentList.sumOf { it.amount }
                    val balance = tripItem.budget - expenseTotalAmount
                    BudgetProgressBar(
                        budget = tripItem.budget,
                        balance = balance,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )

                    Text(
                        text = stringResource(R.string.title_expenses),
                        style = StylesCustom.body1,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 24.dp)
                    )

                    //Вкладки
                    LazyRow {
                        items(ExpenseCategory.entries) { item ->
                            ExpenseCategoryTab(
                                categoryTab = item,
                                isSelected = selectedCategoryTab == item,
                                onClick = {
                                    viewModel.processEvent(
                                        TripInfoScreenEvent.OnTabCategorySelected(
                                            tab = item
                                        )
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn {
                        items(currentListFiltered) { expense ->
                            ExpenseItem(
                                expense = expense,
                                onItemClick = {
                                    if (selectedTab == ExpenseTab.ACTUAL) {
                                        viewModel.processEvent(
                                            TripInfoScreenEvent.OnActualExpenseItemClick(
                                                expenseId = expense.id,
                                                tripId = tripId
                                            )
                                        )
                                    }
                                },
                                onIconClick = {
                                    if (selectedTab == ExpenseTab.PLANNED) {
                                        viewModel.processEvent(
                                            TripInfoScreenEvent.OnDeletePlannedExpenseIconClick(
                                                expenseId = expense.id
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    if (isCreator) {
                        OutlinedButton(
                            shape = RoundedCornerShape(DimensionsCustom.roundedCorners),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.error,
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            onClick = {
                                viewModel.processEvent(
                                    TripInfoScreenEvent.OnFinishBtnClick(
                                        tripId
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.btn_finish_trip),
                                color = MaterialTheme.colorScheme.error,
                                style = StylesCustom.body3,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                            )
                        }
                    }
                }

                is TripInfoScreenState.Loading -> {
                    ShimmerCustom(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DimensionsCustom.tripCardHeight)
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ShimmerCustom(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)

                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ShimmerCustom(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    )

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.shimmerTextHeight)
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )

                    ShimmerCustom(
                        modifier = Modifier
                            .height(DimensionsCustom.shimmerTextHeight)
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 24.dp)
                    )

                    LazyRow {
                        items(6) {
                            ShimmerCustom(
                                modifier = Modifier
                                    .height(DimensionsCustom.shimmerTextHeight)
                                    .fillMaxWidth()
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(DimensionsCustom.roundedCornersSmall))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn {
                        items(10) { expense ->
                            ShimmerCustom(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DimensionsCustom.expenseCardHeight)
                                    .padding(bottom = 16.dp)
                                    .clip(RoundedCornerShape(DimensionsCustom.roundedCorners))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    expense: ExpenseModel,
    onItemClick: () -> Unit = {},
    onIconClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(DimensionsCustom.expenseCardHeight)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(DimensionsCustom.roundedCorners))
                .clickable { onItemClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = expense.title,
                    style = StylesCustom.h4,
                )
                Row {
                    Text(
                        text = expense.amount.toString(),
                        style = StylesCustom.body3
                    )
                    Icon(
                        imageVector = IconsCustom.rubIcon(),
                        contentDescription = ""
                    )
                }
            }
        }
        if (expense is PlannedExpenseModel) {
            Icon(
                imageVector = IconsCustom.crossCircleIcon(),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(DimensionsCustom.iconSizeExtraMini)
                    .clickable {
                        onIconClick()
                    }
            )
        }
    }
}

@Composable
fun BudgetProgressBar(
    budget: Double,
    balance: Double,
    modifier: Modifier = Modifier
) {

    val progress = if (balance > 0) balance / budget else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        // Заголовок с балансом
        Text(
            text = "%.0f ₽".format(balance),
            style = StylesCustom.budgetText,
            color = if (balance < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Прогресс-бар
        LinearProgressIndicator(
            progress = { progress.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            gapSize = 0.dp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Подписи под прогресс-баром
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0",
                style = StylesCustom.miniLabel,
                color = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                text = "%.0f ₽".format(budget),
                style = StylesCustom.miniLabel,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
private fun ExpenseCategoryTab(
    categoryTab: ExpenseCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        border = if (!isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.outline) else null,
        colors = if (!isSelected) CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.secondary
        ) else CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = Modifier
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(DimensionsCustom.roundedCornersSmall))
            .clickable { onClick() }
    ) {
        Text(
            text = stringResource(categoryTab.stringResourceId),
            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
            style = StylesCustom.body4Light,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun rememberViewModel(): TripInfoViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun TripsInfoScreenPreview() {
    TTripsTheme {
        TripInfoScreen(0)
    }
}