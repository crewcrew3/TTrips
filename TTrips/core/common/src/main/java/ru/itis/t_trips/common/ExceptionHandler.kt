package ru.itis.t_trips.common

import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.ui.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandler @Inject constructor(
    private val navigator: Navigator
) {
    fun handleExceptionMessage(exceptionMessage: String?) : Int {
        return when (exceptionMessage) {
            ExceptionCode.NOT_FOUND -> R.string.exception_msg_not_found
            ExceptionCode.FORBIDDEN -> R.string.exception_msg_forbidden
            ExceptionCode.INVALID_DATA -> R.string.exception_msg_invalid_data_input
            ExceptionCode.WRONG_CREDENTIALS -> R.string.exception_msg_auth_wrong_credentials
            ExceptionCode.UNAUTHORIZED -> {
                navigator.toAuthScreen()
                R.string.exception_msg_unauthorized
            }
            ExceptionCode.USER_EXISTS_RESPONSE -> R.string.exception_msg_user_exist
            ExceptionCode.AUTH_TOKEN_RESPONSE -> R.string.exception_msg_auth
            ExceptionCode.TRIP_RESPONSE -> R.string.exception_msg_trip
            ExceptionCode.PROFILE_RESPONSE ->  R.string.exception_msg_profile
            ExceptionCode.UPDATE_USER_CREDENTIALS -> R.string.exception_msg_update_data
            ExceptionCode.EXPENSE_ACTUAL_RESPONSE -> R.string.exception_msg_actual_expenses
            ExceptionCode.EXPENSE_PLANNED_RESPONSE -> R.string.exception_msg_planned_expenses
            ExceptionCode.INVITATION_RESPONSE -> R.string.exception_msg_invitation
            ExceptionCode.INVITATION_DETAILS_RESPONSE -> R.string.exception_msg_invitation_details
            ExceptionCode.EXPENSE_SAVE_ACTUAL_RESPONSE -> R.string.exception_msg_save_actual_expense
            ExceptionCode.EXPENSE_SAVE_PLANNED_RESPONSE -> R.string.exception_msg_save_planned_expense
            ExceptionCode.DEBT_RESPONSE -> R.string.exception_msg_debt
            else -> R.string.exception_msg_common
        }
    }
}