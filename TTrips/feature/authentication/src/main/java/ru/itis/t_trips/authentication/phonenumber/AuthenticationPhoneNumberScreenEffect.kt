package ru.itis.t_trips.authentication.phonenumber

import androidx.annotation.StringRes

internal sealed interface AuthenticationPhoneNumberScreenEffect {
    data class Error(@StringRes val message: Int) : AuthenticationPhoneNumberScreenEffect
    data object ErrorInput : AuthenticationPhoneNumberScreenEffect
}