package ru.itis.t_trips.authentication.password

import androidx.annotation.StringRes

internal sealed interface AuthenticationPasswordScreenEffect {
    data class Error(@StringRes val message: Int) : AuthenticationPasswordScreenEffect
}