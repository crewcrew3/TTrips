package ru.itis.t_trips.addtrip.commoninfo

import android.net.Uri
import ru.itis.t_trips.ui.model.Contact

data class AddTripFormState(
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val startDateUi: String = "",
    val endDateUi: String = "",
    val budget: String = "",
    val uri: Uri? = null,
)