package ru.itis.t_trips.authentication.password

internal sealed interface AuthenticationPasswordScreenEvent {
    data class OnLogInBtnClick(val phoneNumber: String, val password: String) : AuthenticationPasswordScreenEvent
    data object GoToTripsListScreen : AuthenticationPasswordScreenEvent
    data class OnFormFieldChanged(
        val password: String? = null,
    ) : AuthenticationPasswordScreenEvent
}