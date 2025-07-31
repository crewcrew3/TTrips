package ru.itis.profile

internal sealed interface ProfileScreenEvent {
    data object OnInitProfile : ProfileScreenEvent
    data class OnEditProfileBtnClick(val firstName: String, val lastName: String, val userPhotoUrl: String?) : ProfileScreenEvent
    data object OnTripArchiveTabClick : ProfileScreenEvent
    data object OnLogOutTabClick : ProfileScreenEvent
    data class OnPrivacyTabClick(val phoneNumber: String) : ProfileScreenEvent
    data class OnChangeLocale(val locale: String) : ProfileScreenEvent
}