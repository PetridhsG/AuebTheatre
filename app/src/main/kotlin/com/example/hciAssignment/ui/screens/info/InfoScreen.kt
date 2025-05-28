package com.example.hciAssignment.ui.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hciAssignment.ui.screens.info.model.InfoContract
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    viewModel: InfoViewModel = koinViewModel(),
    onBackButtonClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.setEvent(InfoContract.Event.Init)
        viewModel.uiEffect.collect { uiEffect ->
            if (uiEffect is InfoContract.Effect.GoBack) onBackButtonClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("How to Use", fontSize = 22.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Welcome!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Explore theatre info, chat with an assistant, and manage your bookings easily.",
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                )
            }

            item {
                SectionHeader("Home Screen")
                SectionText(
                    "• Choose from 5 quick-start options.\n" +
                            "• Or tap 'Start Conversation' to begin a custom conversation."
                )
            }

            item {
                SectionHeader("Chat Assistant")
                SectionText(
                    "• Ask about theatre info, plays, tickets, or contact support.\n" +
                            "• If the assistant doesn't understand, a human representative is suggested.\n" +
                            "• All chatbot options are also available via the manual menu."
                )
            }

            item {
                SectionHeader("Manual Menu")
                SectionText(
                    "• Use the top-left menu to access features manually.\n" +
                            "• Useful if you prefer not to use the chat."
                )
            }

            item {
                SectionHeader("Ticket Management")
                SectionText(
                    "• Book, view, or cancel tickets via the chatbot or navigation menu.\n" +
                            "• Simple and quick access."
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun SectionText(text: String) {
    Text(
        text = text,
        fontSize = 17.sp,
        lineHeight = 24.sp
    )
}
