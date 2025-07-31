package ru.itis.t_trips.invitationdetails

import androidx.annotation.StringRes

internal sealed interface InvitationDetailsScreenEffect {
    data class Message(@StringRes val message: Int) : InvitationDetailsScreenEffect
}