package ru.itis.t_trips.editprofile.profiledata

import android.net.Uri

internal sealed interface EditProfileScreenEvent {
    data object OnBackBtnClick: EditProfileScreenEvent
    data class OnSaveBtnClick(val firstName: String, val lastName: String, val uri: Uri?): EditProfileScreenEvent
    data class OnFormFieldChanged(
        val firstName: String? = null,
        val lastName: String? = null,
        val uri: Uri? = null
    ): EditProfileScreenEvent
}