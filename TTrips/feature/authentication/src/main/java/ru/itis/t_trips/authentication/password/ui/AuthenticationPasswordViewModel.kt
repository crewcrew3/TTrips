package ru.itis.t_trips.authentication.password.ui

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
import ru.itis.t_trips.authentication.password.AuthenticationPasswordScreenEffect
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.authentication.password.AuthenticationPasswordScreenEvent
import ru.itis.t_trips.authentication.password.AuthenticationPasswordScreenState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.exception.AuthenticationException
import ru.itis.t_trips.domain.usecase.auth.AuthenticateUserUseCase
import ru.itis.t_trips.navigation_api.navigator.AuthNavigator
import javax.inject.Inject

@HiltViewModel
internal class AuthenticationPasswordViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val authNavigator: AuthNavigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<AuthenticationPasswordScreenState>(value = AuthenticationPasswordScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = AuthFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<AuthenticationPasswordScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: AuthenticationPasswordScreenEvent) {
        when (event) {
            is AuthenticationPasswordScreenEvent.OnLogInBtnClick -> authUser(phoneNumber = event.phoneNumber, password = event.password)
            is AuthenticationPasswordScreenEvent.GoToTripsListScreen -> authNavigator.toTripsListScreen()
            is AuthenticationPasswordScreenEvent.OnFormFieldChanged ->  {
                _formState.update { state ->
                    state.copy(
                        password = event.password ?: state.password,
                    )
                }
            }
        }
    }

    private fun authUser(phoneNumber: String, password: String) {
        //для теста навигации
        //_pageState.value = AuthenticationPasswordScreenState.OnLogInSuccess
        viewModelScope.launch {
            runCatching {
                authenticateUserUseCase(phoneNumber, password)
            }.onSuccess {
                _pageState.value = AuthenticationPasswordScreenState.OnLogInSuccess
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    AuthenticationPasswordScreenEffect.Error(
                        message = messageResId
                    )
                )
            }
        }
    }
}