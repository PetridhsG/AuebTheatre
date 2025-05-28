package com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hciAssignment.R
import com.example.hciAssignment.ui.screens.chatbot.ChatbotViewModel
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract

@Composable
fun SeatSelectionBottomSheetFrame(
    state: ChatbotContract.State,
    viewModel: ChatbotViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Row for the cancel button and title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_seat_selection_frame_select_seats_text),
                style = MaterialTheme.typography.titleMedium
            )

            // X button for canceling the selection
            IconButton(
                onClick = {
                    viewModel.setEvent(ChatbotContract.Event.OnCancelBookingClicked)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // Seat selection logic
            state.selectedPerformance?.let { performance ->
                val seatRows = performance.seats.groupBy { it.row }

                seatRows.forEach { (_, seatsInRow) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        seatsInRow.forEach { seat ->
                            val isSelected = state.selectedSeats.contains(seat)

                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when {
                                            !seat.isAvailable -> Color.Red
                                            isSelected -> Color.Green
                                            else -> Color.LightGray
                                        }
                                    )
                                    .clickable(enabled = seat.isAvailable) {
                                        viewModel.setEvent(
                                            ChatbotContract.Event.OnSeatSelected(seat)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${seat.row}${seat.column}",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.bottom_sheet_seat_selection_frame_seat_limit_text),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    viewModel.setEvent(ChatbotContract.Event.OnNextButtonClicked)
                },
                enabled = state.isNextButtonSeatSelectionEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Next (${state.selectedSeats.size}/4)")
            }
        }
    }
}
