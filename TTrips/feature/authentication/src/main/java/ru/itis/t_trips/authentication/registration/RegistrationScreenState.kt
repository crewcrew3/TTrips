package ru.itis.t_trips.authentication.registration

internal sealed interface RegistrationScreenState {
    data object Initial : RegistrationScreenState
    data object OnSignUpSuccess : RegistrationScreenState
}