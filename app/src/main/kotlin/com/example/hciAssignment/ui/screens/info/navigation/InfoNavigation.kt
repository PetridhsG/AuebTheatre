package com.example.hciAssignment.ui.screens.info.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.hciAssignment.ui.screens.info.InfoScreen
import kotlinx.serialization.Serializable

@Serializable
data object InfoRoute

fun NavController.navigateToInfo(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = InfoRoute, navOptions)
}

fun NavGraphBuilder.infoScreen(
    navigateToChatbot: () -> Unit
) {
    composable<InfoRoute> {
        InfoScreen(onBackButtonClick = navigateToChatbot)
    }
}
