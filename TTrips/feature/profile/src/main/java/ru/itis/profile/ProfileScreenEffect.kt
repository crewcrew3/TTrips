package ru.itis.profile

import androidx.annotation.StringRes

internal sealed interface ProfileScreenEffect {
    data class Error(@StringRes val message: Int) : ProfileScreenEffect
}