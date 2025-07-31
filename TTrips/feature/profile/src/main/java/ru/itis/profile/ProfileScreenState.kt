package ru.itis.profile

import ru.itis.t_trips.domain.model.UserProfileModel

internal sealed interface ProfileScreenState {
    data object Initial : ProfileScreenState
    data object Loading : ProfileScreenState
    data class UserProfileResult(val result: UserProfileModel, val photoUrl: String?) : ProfileScreenState
}