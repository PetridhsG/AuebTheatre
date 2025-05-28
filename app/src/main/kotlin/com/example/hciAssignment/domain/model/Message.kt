package com.example.hciAssignment.domain.model

data class Message(
    val message: String,
    val isFromUser: Boolean,
    val isTypingIndicator: Boolean = false,
    val messageType: MessageType = MessageType.OTHER
)

enum class MessageType {
    SELECT_PERFORMANCE,
    CONTACT,
    CONTACT_DETAILS,
    OTHER
}
