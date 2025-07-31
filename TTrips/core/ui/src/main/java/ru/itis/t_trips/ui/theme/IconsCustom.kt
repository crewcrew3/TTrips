package ru.itis.t_trips.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.itis.t_trips.ui.R

object IconsCustom {

    @Composable
    fun tripsIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_trips)

    @Composable
    fun profileIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_profile)

    @Composable
    fun notificationsIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_notification)

    @Composable
    fun arrowBackIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back)

    @Composable
    fun arrowForwardIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_arrow_forward)

    @Composable
    fun addContentIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_add)

    @Composable
    fun editIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_pencil)

    @Composable
    fun checkIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_check)

    @Composable
    fun tabArchiveIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_archive)

    @Composable
    fun tabPrivacyIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_privacy)

    @Composable
    fun tabLogOutIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_logout)

    @Composable
    fun deleteIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_trash)

    @Composable
    fun addPhotoIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_add_photo)

    @Composable
    fun calendarIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_calendar)

    @Composable
    fun crossIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_cross)

    @Composable
    fun crossCircleIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_cross_circle)

    @Composable
    fun visibilityIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_visibility)

    @Composable
    fun visibilityOffIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_visibility_off)

    @Composable
    fun sunnyIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_sunny)

    @Composable
    fun rubIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_rubel)

    @Composable
    fun indicatorIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_circle)

    @Composable
    fun receiptIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_receipt)

    @Composable
    fun peopleIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_people)

    @Composable
    fun languageIcon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_language)
}