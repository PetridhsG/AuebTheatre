package com.example.hciAssignment.ui.screens.chatbot.frames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hciAssignment.R
import com.example.hciAssignment.ui.screens.chatbot.ChatbotViewModel
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFrame(
    viewModel: ChatbotViewModel
) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.setEvent(Event.OnTicketsButtonClicked)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ticket_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.top_app_bar_title),
                            fontSize = 32.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.setEvent(Event.OnInfoButtonClicked)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_help_text),
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HomeOptions(viewModel = viewModel)

            Spacer(modifier = Modifier.padding(30.dp))

            ChatStartButton {
                viewModel.setEvent(Event.OnGoToChatButtonClicked)
            }
        }
    }
}

@Composable
private fun HomeOptions(viewModel: ChatbotViewModel) {
    Column(
        modifier = Modifier
            .padding(top = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeOptionButton(
            iconResId = R.drawable.baseline_info_outline_24,
            labelResId = R.string.home_learn_about_theatre_message,
            onClick = { viewModel.setEvent(Event.OnLearnAboutTheatreClicked) }
        )

        HomeOptionButton(
            iconResId = R.drawable.baseline_theater_comedy_24,
            labelResId = R.string.home_learn_about_plays_message,
            onClick = { viewModel.setEvent(Event.OnLearnAboutPlaysClicked) }
        )

        HomeOptionButton(
            iconResId = R.drawable.baseline_event_seat_24,
            labelResId = R.string.home_book_tickets_message,
            onClick = { viewModel.setEvent(Event.OnReserveSeatsClicked) }
        )

        HomeOptionButton(
            iconResId = R.drawable.baseline_receipt_long_24,
            labelResId = R.string.home_check_tickets_message,
            onClick = { viewModel.setEvent(Event.OnViewReservationsClicked) }
        )

        HomeOptionButton(
            iconResId = R.drawable.baseline_local_phone_24,
            labelResId = R.string.home_contact_message,
            onClick = { viewModel.setEvent(Event.OnContactWithRepresentativeClicked) }
        )
    }
}

@Composable
private fun HomeOptionButton(
    iconResId: Int,
    labelResId: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 4.dp)
        )
        Text(text = stringResource(id = labelResId))
    }
}

@Composable
private fun ChatStartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(260.dp, 60.dp)
    ) {
        Text(
            text = stringResource(R.string.home_chat_message),
            fontSize = 20.sp
        )
    }
}
