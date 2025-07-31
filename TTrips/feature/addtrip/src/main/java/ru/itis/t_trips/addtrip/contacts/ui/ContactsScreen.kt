package ru.itis.t_trips.addtrip.contacts.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.addtrip.contacts.ContactsScreenEffect
import ru.itis.t_trips.addtrip.contacts.ContactsScreenEvent
import ru.itis.t_trips.addtrip.contacts.ContactsScreenState
import ru.itis.t_trips.ui.BaseScreen
import ru.itis.t_trips.ui.components.DividerCustom
import ru.itis.t_trips.ui.components.UserMiniCardCustom
import ru.itis.t_trips.ui.components.settings.FloatingActionButtonSettings
import ru.itis.t_trips.ui.components.settings.TopBarSettings
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun ContactsScreen() {

    val viewModel: ContactsViewModel = rememberViewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.pageEffect.collect { effect ->
            when (effect) {
                is ContactsScreenEffect.Message -> Toast.makeText(
                    context,
                    context.getText(effect.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val pageState by viewModel.pageState.collectAsState(initial = ContactsScreenState.Initial)
    when (pageState) {
        is ContactsScreenState.Initial -> viewModel.processEvent(ContactsScreenEvent.OnScreenInit)
        is ContactsScreenState.Result -> Unit
    }

    // Храним локальную копию списка контактов с выбором
    val initialContacts = (pageState as? ContactsScreenState.Result)?.contacts ?: emptyList()
    var contactsWithSelection by remember(initialContacts) { mutableStateOf(initialContacts) }

    // isNext = true, если выбран хотя бы один контакт
    var isNext by remember { mutableStateOf(false) }

    BaseScreen(
        isTopBar = true,
        topBarSettings = TopBarSettings(
            topAppBarText = stringResource(R.string.top_bar_header_add_members),
            onBackPressed = {
                viewModel.processEvent(
                    ContactsScreenEvent.OnBackBtnClick
                )
            },
        ),
        isFloatingActionButton = isNext,
        floatingActBtnSettings = FloatingActionButtonSettings.CircleButtonSettings(
            icon = IconsCustom.arrowForwardIcon(),
            onClick = {
                viewModel.processEvent(
                    ContactsScreenEvent.OnNextBtnClick(
                        contacts = contactsWithSelection.filter { contact -> contact.isChosen}
                    )
                )
            }
        )
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            item {
                Spacer(modifier = Modifier.height(DimensionsCustom.spaceInputFields))
                DividerCustom()
                Text(
                    text = stringResource(R.string.contacts_screen_text),
                    style = StylesCustom.body4,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }

            items(initialContacts) { contact ->
                UserMiniCardCustom(
                    contact = contact,
                    onChosenChange = { chosen ->
                        contactsWithSelection = contactsWithSelection.map {
                            if (it == contact) it.copy(isChosen = chosen) else it
                        }
                        isNext = contactsWithSelection.any { it.isChosen }
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberViewModel(): ContactsViewModel = hiltViewModel()

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun ContactsScreenPreview() {
    TTripsTheme {
        ContactsScreen()
    }
}