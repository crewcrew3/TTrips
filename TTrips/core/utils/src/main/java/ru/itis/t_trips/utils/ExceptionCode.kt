package ru.itis.t_trips.utils

object ExceptionCode {

    const val NAV_CONTROLLER_NOT_PROVIDED = "NavController not provided!"

    const val AUTH_TOKEN_RESPONSE = "auth_token_response_error"
    const val EXPENSE_ACTUAL_RESPONSE = "expense_actual_response_error"
    const val EXPENSE_SAVE_ACTUAL_RESPONSE = "expense_save_actual_response_error"
    const val EXPENSE_PLANNED_RESPONSE = "expense_planned_response_error"
    const val EXPENSE_SAVE_PLANNED_RESPONSE = "expense_save_planned_response_error"
    const val TRIP_RESPONSE = "trip_response_error"
    const val PROFILE_RESPONSE = "profile_response_error"
    const val UPDATE_USER_CREDENTIALS = "user_credentials_response_error"
    const val USER_EXISTS_RESPONSE = "user_exists_response_error"
    const val INVITATION_RESPONSE = "invitation_response_error"
    const val INVITATION_DETAILS_RESPONSE = "invitation_details_response_error"
    const val DEBT_RESPONSE = "debt_response_error"

    const val TRIP_MEMBERS_RESPONSE = "trip_members_response_error"
    const val LOG_OUT_RESPONSE = "log_out_response_error"
    const val DELETE_TRIP_RESPONSE = "delete_trip_response_error"

    const val WRONG_CREDENTIALS = "wrong_credentials_error"
    const val UNAUTHORIZED = "unauthorized_error"
    const val INVALID_DATA = "invalid_data"
    const val NOT_FOUND = "not_found"
    const val FORBIDDEN = "forbidden"

    const val UNKNOWN_PICTURE_KEY = "unknown_picture_key"
}