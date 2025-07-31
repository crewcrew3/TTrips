package ru.itis.t_trips.editprofile.profiledata

import androidx.annotation.StringRes

internal sealed interface EditProfileScreenEffect {
    data class Message(@StringRes val message: Int) : EditProfileScreenEffect
    data object ErrorFirstNameInput : EditProfileScreenEffect
    data object ErrorLastNameInput : EditProfileScreenEffect
}