package ru.itis.t_trips.editprofile.privacy

import androidx.annotation.StringRes

internal sealed interface PrivacyScreenEffect  {
    data class Message(@StringRes val message: Int) : PrivacyScreenEffect
    data object ErrorInput : PrivacyScreenEffect
}