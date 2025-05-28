package com.example.hciAssignment.domain.model

data class BotResponse(
    val intent: ChatbotIntent,
    val responseMessage: String
)

enum class ChatbotIntent {
    THEATRE_INFO,
    PLAYS_INFO,
    SELECT_PERFORMANCE,
    PROCEED_TO_BOOKING,
    BOOKING,
    VIEW_TICKETS,
    CONTACT,
    UNKNOWN;

    companion object {
        fun fromRaw(intent: String): ChatbotIntent =
            entries.find { it.name.equals(intent, ignoreCase = true) } ?: UNKNOWN
    }
}
