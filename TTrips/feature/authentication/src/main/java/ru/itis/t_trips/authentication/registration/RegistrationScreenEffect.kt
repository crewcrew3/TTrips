package ru.itis.t_trips.authentication.registration

import androidx.annotation.StringRes

internal sealed interface RegistrationScreenEffect {
    data class Error(@StringRes val message: Int) : RegistrationScreenEffect
    data object ErrorFirstNameInput : RegistrationScreenEffect
    data object ErrorLastNameInput : RegistrationScreenEffect
    data object ErrorPasswordInput : RegistrationScreenEffect
    data object ErrorRepeatPasswordInput : RegistrationScreenEffect
}