package com.example.hciAssignment.ui.screens.tickets.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.hciAssignment.ui.screens.tickets.TicketsScreen
import kotlinx.serialization.Serializable

@Serializable
data object TicketsRoute

fun NavController.navigateToTickets(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = TicketsRoute, navOptions)
}

fun NavGraphBuilder.ticketsScreen(
    navigateToChatbot: () -> Unit
) {
    composable<TicketsRoute> {
        TicketsScreen(onBackButtonClick = navigateToChatbot)
    }
}
