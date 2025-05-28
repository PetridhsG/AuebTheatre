package com.example.hciAssignment.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val message: ChatMessage
)
