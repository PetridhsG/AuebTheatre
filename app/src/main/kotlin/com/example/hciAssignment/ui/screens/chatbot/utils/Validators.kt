package com.example.hciAssignment.ui.screens.chatbot.utils

import com.example.hciAssignment.R
import com.example.hciAssignment.resources.ResourceProvider

fun validateNameInput(name: String, resourceProvider: ResourceProvider): String? {
    return when {
        name.isBlank() -> resourceProvider.getString(R.string.validator_name_blank)
        name.length < 4 -> resourceProvider.getString(R.string.validator_name_too_short)
        else -> null
    }
}

fun validateSurnameInput(surname: String, resourceProvider: ResourceProvider): String? {
    return when {
        surname.isBlank() -> resourceProvider.getString(R.string.validator_surname_blank)
        surname.length < 4 -> resourceProvider.getString(R.string.validator_surname_too_short)
        else -> null
    }
}

fun validateEmailInput(email: String, resourceProvider: ResourceProvider): String? {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    return when {
        email.isBlank() -> resourceProvider.getString(R.string.validator_email_blank)
        !email.matches(Regex(emailPattern)) -> resourceProvider.getString(R.string.validator_email_invalid)
        else -> null
    }
}

fun validateTelephoneInput(phone: String, resourceProvider: ResourceProvider): String? {
    return when {
        phone.isBlank() -> resourceProvider.getString(R.string.validator_telephone_blank)
        phone.length < 7 -> resourceProvider.getString(R.string.validator_telephone_too_short)
        !phone.all { it.isDigit() } -> resourceProvider.getString(R.string.validator_telephone_invalid_chars)
        else -> null
    }
}

fun validateCardNumberInput(cardNumber: String, resourceProvider: ResourceProvider): String? {
    return when {
        cardNumber.isBlank() -> resourceProvider.getString(R.string.validator_card_number_blank)
        !cardNumber.matches(Regex("\\d{16}")) -> resourceProvider.getString(R.string.validator_card_number_invalid)
        else -> null
    }
}

fun validateExpiryDateInput(date: String, resourceProvider: ResourceProvider): String? {
    return when {
        date.isBlank() -> resourceProvider.getString(R.string.validator_expiry_date_blank)
        !date.matches(Regex("(0[1-9]|1[0-2])/\\d{2}")) ->
            resourceProvider.getString(R.string.validator_expiry_date_invalid)
        else -> null
    }
}

fun validateCvvInput(cvv: String, resourceProvider: ResourceProvider): String? {
    return when {
        cvv.isBlank() -> resourceProvider.getString(R.string.validator_cvv_blank)
        !cvv.matches(Regex("\\d{3}")) -> resourceProvider.getString(R.string.validator_cvv_invalid)
        else -> null
    }
}

fun validateCardholderNameInput(name: String, resourceProvider: ResourceProvider): String? {
    return when {
        name.isBlank() -> resourceProvider.getString(R.string.validator_cardholder_name_blank)
        name.length < 4 -> resourceProvider.getString(R.string.validator_cardholder_name_too_short)
        else -> null
    }
}
