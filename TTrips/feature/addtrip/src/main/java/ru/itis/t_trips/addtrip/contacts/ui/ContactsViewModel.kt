package ru.itis.t_trips.addtrip.contacts.ui

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.t_trips.addtrip.contacts.ContactsScreenEffect
import ru.itis.t_trips.addtrip.contacts.ContactsScreenEvent
import ru.itis.t_trips.addtrip.contacts.ContactsScreenState
import ru.itis.t_trips.ui.model.Contact
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.navigator.TripNavigator
import javax.inject.Inject

@HiltViewModel
internal class ContactsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tripNavigator: TripNavigator,
    private val navigator: Navigator,
) : ViewModel() {

    private val _pageState = MutableStateFlow<ContactsScreenState>(value = ContactsScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<ContactsScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: ContactsScreenEvent) {
        when (event) {
            is ContactsScreenEvent.OnScreenInit -> loadContacts()
            is ContactsScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is ContactsScreenEvent.OnNextBtnClick -> tripNavigator.toAddTripScreen(
                contacts = Json.encodeToString(event.contacts)
            )
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            val resultList = mutableListOf<Contact>()
            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, //CONTENT_URI — это URI для доступа к телефонным номерам в базе контактов
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC" //сортировка по имени контакта в порядке возрастания
            )

            cursor?.use {
                //Получаем индексы столбцов DISPLAY_NAME и NUMBER в курсор-результате, чтобы потом быстро получать значения по этим индексам.
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) { //переводит курсор на следующую запись и возвращает false, когда данных больше нет.
                    //Для текущей записи получаем имя и номер телефона.
                    val name = it.getString(nameIndex) ?: ""
                    val number = it.getString(numberIndex) ?: ""
                    resultList.add(
                        Contact(
                            name = name,
                            phoneNumber = number.filter { char -> char.isDigit() } //сразу положим в модель в правильном формате 7ХХХХХХХХХХ
                        )
                    )
                }
            }
            _pageState.value = ContactsScreenState.Result(contacts = resultList)
        }
    }
}