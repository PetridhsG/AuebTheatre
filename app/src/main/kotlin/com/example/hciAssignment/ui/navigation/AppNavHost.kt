package com.example.hciAssignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.hciAssignment.ui.screens.chatbot.navigation.ChatbotRoute
import com.example.hciAssignment.ui.screens.chatbot.navigation.chatbotScreen
import com.example.hciAssignment.ui.screens.info.navigation.infoScreen
import com.example.hciAssignment.ui.screens.info.navigation.navigateToInfo
import com.example.hciAssignment.ui.screens.tickets.navigation.navigateToTickets
import com.example.hciAssignment.ui.screens.tickets.navigation.ticketsScreen

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ChatbotRoute,
    ) {
        chatbotScreen(navController::navigateToInfo, navController::navigateToTickets)
        infoScreen(navController::navigateUp)
        ticketsScreen(navController::navigateUp)
    }
}
