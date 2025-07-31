package ru.itis.t_trips.authentication.registration.ui

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
import ru.itis.t_trips.authentication.phonenumber.AuthenticationPhoneNumberScreenEvent
import ru.itis.t_trips.authentication.registration.RegistrationScreenEffect
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.authentication.registration.RegistrationScreenEvent
import ru.itis.t_trips.authentication.registration.RegistrationScreenState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.exception.AuthenticationException
import ru.itis.t_trips.domain.usecase.auth.RegisterUserUseCase
import ru.itis.t_trips.navigation_api.navigator.AuthNavigator
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.ValidatorHelper
import javax.inject.Inject

@HiltViewModel
internal class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val authNavigator: AuthNavigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<RegistrationScreenState>(value = RegistrationScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = AuthFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<RegistrationScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: RegistrationScreenEvent) {
        when (event) {
            is RegistrationScreenEvent.OnSignUpBtnClick -> registerUser(
                firstName = event.firstName,
                lastName = event.lastName,
                phoneNumber = event.phoneNumber,
                password = event.password,
                repeatPassword = event.repeatPassword
            )

            is RegistrationScreenEvent.GoToTripsListScreen -> authNavigator.toTripsListScreen()

            is RegistrationScreenEvent.OnFormFieldChanged -> {
                _formState.update { state ->
                    state.copy(
                        firstName = event.firstName ?: state.firstName,
                        lastName = event.lastName ?: state.lastName,
                        password = event.password ?: state.password,
                        repeatPassword = event.repeatPassword ?: state.repeatPassword,
                    )
                }
            }
        }
    }

    private fun registerUser(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String,
        repeatPassword: String
    ) {
        viewModelScope.launch {
            if (validateRegisterForm(
                    firstName = firstName,
                    lastName = lastName,
                    password = password,
                    repeatPassword = repeatPassword,
                )
            ) {
                runCatching {
                    registerUserUseCase(
                        firstName,
                        lastName,
                        phoneNumber,
                        password,
                        repeatPassword
                    )
                }.onSuccess {
                    _pageState.value = RegistrationScreenState.OnSignUpSuccess
                }.onFailure { exception ->
                    val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                    _pageEffect.emit(
                        RegistrationScreenEffect.Error(
                            message = messageResId
                        )
                    )
                }
            }
        }
    }

    private suspend fun validateRegisterForm(
        firstName: String,
        lastName: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        var errorCounts = 0
        if (!ValidatorHelper.validateFirstName(firstName)) {
            _pageEffect.emit(RegistrationScreenEffect.ErrorFirstNameInput)
            errorCounts++
        }
        if (!ValidatorHelper.validateLastName(lastName)) {
            _pageEffect.emit(RegistrationScreenEffect.ErrorLastNameInput)
            errorCounts++
        }
        if (!ValidatorHelper.validatePassword(password)) {
            _pageEffect.emit(RegistrationScreenEffect.ErrorPasswordInput)
            errorCounts++
        }
        if (password != repeatPassword) {
            _pageEffect.emit(RegistrationScreenEffect.ErrorRepeatPasswordInput)
            errorCounts++
        }
        return errorCounts == 0
    }
}