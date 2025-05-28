package com.example.hciAssignment.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val intent: String
)
