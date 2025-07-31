package ru.itis.t_trips.authentication.phonenumber

internal sealed interface AuthenticationPhoneNumberScreenEvent {
    data class OnContinueBtnClick(val phoneNumber: String) : AuthenticationPhoneNumberScreenEvent
    data class OnFormFieldChanged(
        val phoneNumber: String? = null,
    ) : AuthenticationPhoneNumberScreenEvent
}