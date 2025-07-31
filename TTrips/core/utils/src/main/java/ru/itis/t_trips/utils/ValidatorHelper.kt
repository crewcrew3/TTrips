package ru.itis.t_trips.utils

object ValidatorHelper {

    private const val REGEX_FOR_PHONE_NUMBER = "^7\\d{10}$"

    fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex(REGEX_FOR_PHONE_NUMBER)
        return regex.matches(phoneNumber)
    }

    fun validateFirstName(firstName: String): Boolean {
        return (firstName.length <= 25) && (firstName.isNotBlank())
    }

    fun validateLastName(lastName: String): Boolean {
        return (lastName.length <= 25) && (lastName.isNotBlank())
    }

    fun validatePassword(password: String): Boolean {
        return (8 <= password.length) && (password.length <= 100) && (password.isNotBlank())
    }

    fun validateTripTitle(title: String): Boolean {
        return (title.length <= 100) && (title.isNotBlank())
    }

    fun validateExpenseTitle(title: String): Boolean {
        return title.isNotBlank() && (title.length <= 500)
    }
}