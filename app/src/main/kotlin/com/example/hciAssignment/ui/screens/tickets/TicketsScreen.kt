package com.example.hciAssignment.ui.screens.tickets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.model.Ticket
import com.example.hciAssignment.ui.screens.tickets.frames.TicketsBottomSheetFrame
import com.example.hciAssignment.ui.screens.tickets.frames.TicketsCheckoutBottomSheetFrame
import com.example.hciAssignment.ui.screens.tickets.frames.TicketsPerformanceSelectionFrame
import com.example.hciAssignment.ui.screens.tickets.frames.TicketsSeatSelectionBottomSheetFrame
import com.example.hciAssignment.ui.screens.tickets.frames.TicketsUserInfoBottomSheetFrame
import com.example.hciAssignment.ui.screens.tickets.model.TicketsContract
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsScreen(
    viewModel: TicketsViewModel = koinViewModel(),
    onBackButtonClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetValue -> sheetValue != SheetValue.Hidden }
    )

    LaunchedEffect(Unit) {
        viewModel.setEvent(TicketsContract.Event.Init)
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                TicketsContract.Effect.GoBack -> onBackButtonClick()

                is TicketsContract.Effect.ShowSnackbar -> {
                    coroutineScope.launch {
                        snackbarState.showSnackbar(uiEffect.message)
                    }
                }

                TicketsContract.Effect.DismissSnackbar -> {
                    snackbarState.currentSnackbarData?.dismiss()
                }

                TicketsContract.Effect.OnDismissBottomSheet -> {
                    bottomSheetState.hide()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.bottom_sheet_view_reservations_frame_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.setEvent(TicketsContract.Event.GoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setEvent(TicketsContract.Event.OnFabClicked) },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ticket_icon),
                        contentDescription =null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Book Tickets",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (state.userTickets.isEmpty()) {
                            Text(
                                text = stringResource(R.string.bottom_sheet_view_reservations_frame_no_reservations),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        } else {
                            LazyColumn {
                                items(state.userTickets) { ticket ->
                                    ReservationCard(
                                        ticket = ticket,
                                        onDelete = {
                                            viewModel.setEvent(
                                                TicketsContract.Event.OnCancelTicketClicked(ticket)
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        ChatDialogs(viewModel, state)

                        if (state.isBottomSheetVisible) {
                            ModalBottomSheet(
                                onDismissRequest = {},
                                sheetState = bottomSheetState
                            ) {
                                BackHandler(enabled = true) {
                                    when (state.visibleBottomSheetFrame) {
                                        TicketsBottomSheetFrame.SELECT_PERFORMANCE -> {
                                            viewModel.setEvent(TicketsContract.Event.OnCancelBookingClicked)
                                        }

                                        TicketsBottomSheetFrame.SEAT_SELECTION -> {
                                            viewModel.setEvent(TicketsContract.Event.OnBackToPerformanceSelectionClicked)
                                        }

                                        TicketsBottomSheetFrame.USER_INFO -> {
                                            viewModel.setEvent(TicketsContract.Event.OnBackToSeatSelectionClicked)
                                        }

                                        TicketsBottomSheetFrame.CHECKOUT -> {
                                            viewModel.setEvent(TicketsContract.Event.OnBackToUserInfoClicked)
                                        }

                                    }
                                }

                                when (state.visibleBottomSheetFrame) {
                                    TicketsBottomSheetFrame.SEAT_SELECTION -> {
                                        TicketsSeatSelectionBottomSheetFrame(state, viewModel)
                                    }

                                    TicketsBottomSheetFrame.USER_INFO -> {
                                        TicketsUserInfoBottomSheetFrame(state, viewModel)
                                    }

                                    TicketsBottomSheetFrame.CHECKOUT -> {
                                        TicketsCheckoutBottomSheetFrame(state, viewModel)
                                    }

                                    TicketsBottomSheetFrame.SELECT_PERFORMANCE -> {
                                        TicketsPerformanceSelectionFrame(state, viewModel)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 110.dp)
            ) { snackbarData ->
                Snackbar(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    shape = RectangleShape
                ) {
                    Text(snackbarData.visuals.message)
                }
            }
        }
    }
}

@Composable
fun ReservationCard(ticket: Ticket, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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

            Spacer(modifier = Modifier.height(2.dp))
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

@Composable
fun ChatDialogs(
    viewModel: TicketsViewModel,
    state: TicketsContract.State
) {
    if (state.isCancelBookingAlertDialogVisible) {
        GenericAlertDialog(
            informativeText = stringResource(R.string.cancel_booking_alert_dialog_informative_text),
            negativeText = stringResource(R.string.cancel_booking_alert_dialog_negative_text),
            positiveText = stringResource(R.string.cancel_booking_alert_dialog_positive_text),
            onConfirm = { viewModel.setEvent(TicketsContract.Event.CancelBookingAlertDialogEvent.OnPositiveClicked) },
            onDismiss = { viewModel.setEvent(TicketsContract.Event.CancelBookingAlertDialogEvent.OnDismissed) },
            onCancel = { viewModel.setEvent(TicketsContract.Event.CancelBookingAlertDialogEvent.OnNegativeClicked) }
        )
    }

    if (state.isConfirmBookingAlertDialogVisible) {
        GenericAlertDialog(
            informativeText = stringResource(R.string.confirm_booking_alert_dialog_informative_text),
            negativeText = stringResource(R.string.confirm_booking_alert_dialog_negative_text),
            positiveText = stringResource(R.string.confirm_booking_alert_dialog_positive_text),
            onConfirm = { viewModel.setEvent(TicketsContract.Event.ConfirmBookingAlertDialogEvent.OnPositiveClicked) },
            onDismiss = { viewModel.setEvent(TicketsContract.Event.ConfirmBookingAlertDialogEvent.OnDismissed) },
            onCancel = { viewModel.setEvent(TicketsContract.Event.ConfirmBookingAlertDialogEvent.OnNegativeClicked) }
        )
    }

    if (state.isCancelTicketAlertDialogVisible) {
        GenericAlertDialog(
            informativeText = stringResource(R.string.cancel_ticket_alert_dialog_informative_text),
            negativeText = stringResource(R.string.cancel_ticket_alert_dialog_negative_text),
            positiveText = stringResource(R.string.cancel_ticket_alert_dialog_positive_text),
            onConfirm = { viewModel.setEvent(TicketsContract.Event.CancelTicketAlertDialogEvent.OnPositiveClicked) },
            onDismiss = { viewModel.setEvent(TicketsContract.Event.CancelTicketAlertDialogEvent.OnDismissed) },
            onCancel = { viewModel.setEvent(TicketsContract.Event.CancelTicketAlertDialogEvent.OnNegativeClicked) }
        )
    }
}

@Composable
fun GenericAlertDialog(
    informativeText: String,
    negativeText: String,
    positiveText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        text = { Text(informativeText) },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(positiveText)
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(negativeText)
            }
        }
    )
}
