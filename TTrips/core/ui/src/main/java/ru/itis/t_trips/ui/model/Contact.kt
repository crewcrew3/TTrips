package ru.itis.t_trips.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Int = 0,
    val name: String,
    val phoneNumber: String = "",
    val isChosen: Boolean = false,
    val amount: Double = -1.0
)