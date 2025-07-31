package ru.itis.t_trips.ui.model.enums

import androidx.annotation.StringRes
import ru.itis.t_trips.ui.R

enum class TripStatus(@StringRes val stringResourceId: Int) {
    ACTIVE(R.string.trip_status_active),
    ARCHIVED(R.string.trip_status_archived);

    companion object {
        fun getStringResourceId(status: String): Int? {
            return entries.find { it.name.equals(status, ignoreCase = true) }?.stringResourceId
        }
    }
}
