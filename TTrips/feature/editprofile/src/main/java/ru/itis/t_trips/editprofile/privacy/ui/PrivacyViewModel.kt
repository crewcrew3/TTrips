package ru.itis.t_trips.editprofile.privacy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.user.UpdateUserCredentialsUseCase
import ru.itis.t_trips.editprofile.ProfileFormState
import ru.itis.t_trips.editprofile.privacy.PrivacyScreenEffect
import ru.itis.t_trips.editprofile.privacy.PrivacyScreenEvent
import ru.itis.t_trips.editprofile.privacy.PrivacyScreenState
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEffect
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEvent
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.utils.ExceptionCode
import javax.inject.Inject

@HiltViewModel
internal class PrivacyViewModel @Inject constructor(
    private val updateUserCredentialsUseCase: UpdateUserCredentialsUseCase,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<PrivacyScreenState>(value = PrivacyScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = ProfileFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<PrivacyScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: PrivacyScreenEvent) {
        when (event) {
            is PrivacyScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is PrivacyScreenEvent.OnSaveBtnClick -> saveNewCredentials(phoneNumber = event.phoneNumber, password = event.password)
            is PrivacyScreenEvent.OnFormFieldChanged -> {
                _formState.update { state ->
                    state.copy(
                        phoneNumber = event.phoneNumber ?: state.phoneNumber,
                        password = event.password ?: state.password,
                    )
                }
            }
        }
    }

    private fun saveNewCredentials(phoneNumber: String, password: String) {
        viewModelScope.launch {
            runCatching {
                updateUserCredentialsUseCase(phoneNumber = phoneNumber, password = password)
            }.onSuccess {
                _pageEffect.emit(PrivacyScreenEffect.Message(R.string.msg_update_data_success))
                navigator.popBackStack()
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    PrivacyScreenEffect.Message(
                        message = messageResId
                    )
                )
            }
        }
    }

}