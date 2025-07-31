package ru.itis.t_trips.authentication.phonenumber.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.t_trips.authentication.AuthFormState
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenEffect
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenEvent
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.auth.CheckUserExistenceUseCase
import ru.itis.t_trips.navigation_api.navigator.AuthNavigator
import ru.itis.t_trips.utils.ValidatorHelper
import javax.inject.Inject

@HiltViewModel
internal class AuthenticationPhoneNumberViewModel @Inject constructor(
    private val checkUserExistenceUseCase: CheckUserExistenceUseCase,
    private val authNavigator: AuthNavigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<AuthenticationPhoneNumberScreenState>(value = AuthenticationPhoneNumberScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = AuthFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<AuthenticationPhoneNumberScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: AuthenticationPhoneNumberScreenEvent) {
        when (event) {
            is AuthenticationPhoneNumberScreenEvent.OnContinueBtnClick -> checkUserExistence(event.phoneNumber)
            is AuthenticationPhoneNumberScreenEvent.OnFormFieldChanged -> {
                _formState.update { state ->
                    state.copy(
                        phoneNumber = event.phoneNumber ?: state.phoneNumber,
                    )
                }
            }
        }
    }

    private fun checkUserExistence(phoneNumber: String) {
        //для теста навигации
        //authNavigator.toAuthenticationPasswordScreen(phoneNumber)
        viewModelScope.launch {
            if (!ValidatorHelper.validatePhoneNumber(phoneNumber)) {
                _pageEffect.emit(AuthenticationPhoneNumberScreenEffect.ErrorInput)
            } else {
                runCatching {
                    checkUserExistenceUseCase(phoneNumber)
                }.onSuccess { isExist ->
                    if (isExist) {
                        authNavigator.toAuthenticationPasswordScreen(phoneNumber)
                    } else {
                        authNavigator.toRegistrationScreen(phoneNumber)
                    }
                }.onFailure { exception ->
                    val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                    _pageEffect.emit(
                        AuthenticationPhoneNumberScreenEffect.Error(
                            message = messageResId
                        )
                    )
                }
            }
        }
    }
}