package ru.itis.t_trips.authentication.password

internal sealed interface AuthenticationPasswordScreenState {
    data object Initial : AuthenticationPasswordScreenState
    data object OnLogInSuccess : AuthenticationPasswordScreenState
}