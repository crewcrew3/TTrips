package ru.itis.t_trips.editprofile.privacy

internal sealed interface PrivacyScreenEvent {
    data object OnBackBtnClick: PrivacyScreenEvent
    data class OnSaveBtnClick(val phoneNumber: String, val password: String): PrivacyScreenEvent
    data class OnFormFieldChanged(
        val phoneNumber: String? = null,
        val password: String? = null,
    ): PrivacyScreenEvent
}