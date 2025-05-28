package com.example.hciAssignment.ui.screens.chatbot.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.hciAssignment.ui.screens.chatbot.ChatbotScreen
import kotlinx.serialization.Serializable

@Serializable
data object ChatbotRoute

fun NavGraphBuilder.chatbotScreen(
    navigateToInfo: () -> Unit,
    navigateToTickets: () -> Unit
) {
    composable<ChatbotRoute> {
        ChatbotScreen(
            onInfoButtonClick = navigateToInfo,
            onTicketsButtonClick = navigateToTickets
        )
    }
}
