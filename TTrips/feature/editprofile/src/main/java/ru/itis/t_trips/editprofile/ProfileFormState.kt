package ru.itis.t_trips.editprofile

import android.net.Uri

data class ProfileFormState(
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val uri: Uri? = null
)