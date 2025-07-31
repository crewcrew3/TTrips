package ru.itis.t_trips.authentication.phonenumber

internal sealed interface AuthenticationPhoneNumberScreenState {
    data object Initial : AuthenticationPhoneNumberScreenState
}