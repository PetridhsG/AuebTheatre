package com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.model.Ticket
import com.example.hciAssignment.ui.screens.chatbot.ChatbotViewModel
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract.Event

@Composable
fun ViewReservationsFrame(
    state: ChatbotContract.State,
    viewModel: ChatbotViewModel,
    snackBarState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.bottom_sheet_view_reservations_frame_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    viewModel.setEvent(Event.OnDismissBottomSheet)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (state.userTickets.isEmpty()) {
                Text(
                    text = stringResource(R.string.bottom_sheet_view_reservations_frame_no_reservations),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            } else {
                LazyColumn {
                    items(state.userTickets) { ticket ->
                        ReservationCard(ticket = ticket, onDelete = {
                            viewModel.setEvent(Event.OnCancelTicketClicked(ticket))
                        })
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackBarState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp),
            snackbar = {
                Snackbar(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    shape = RectangleShape
                ) {
                    Text(it.visuals.message)
                }
            }
        )
    }
}

@Composable
fun ReservationCard(ticket: Ticket, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.bottom_sheet_view_reservations_frame_card_performance,
                        ticket.performance
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.bottom_sheet_view_reservations_frame_delete_icon_content_description)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.bottom_sheet_view_reservations_frame_card_room,
                    ticket.room
                )
            )
            Text(
                text = stringResource(
                    R.string.bottom_sheet_view_reservations_frame_card_seat,
                    ticket.seat
                )
            )
            Text(
                text = stringResource(
                    R.string.bottom_sheet_view_reservations_frame_card_date,
                    ticket.date
                )
            )
            Text(
                text = stringResource(
                    R.string.bottom_sheet_view_reservations_frame_card_time,
                    ticket.time
                )
            )
            Text(
                text = stringResource(
                    R.string.bottom_sheet_view_reservations_frame_card_name,
                    ticket.name
                )
            )
        }
    }
}
