package ru.itis.t_trips.authentication.registration

internal sealed interface RegistrationScreenEvent {
    data class OnSignUpBtnClick(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val password: String,
        val repeatPassword: String,
    ) : RegistrationScreenEvent

    data object GoToTripsListScreen : RegistrationScreenEvent

    data class OnFormFieldChanged(
        val firstName: String? = null,
        val lastName: String? = null,
        val password: String? = null,
        val repeatPassword: String? = null
    ) : RegistrationScreenEvent
}