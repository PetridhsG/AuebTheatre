package com.example.hciAssignment.ui.screens.chatbot.frames

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.model.MessageType
import com.example.hciAssignment.ui.screens.chatbot.ChatbotViewModel
import com.example.hciAssignment.ui.screens.chatbot.components.MessageBubble
import com.example.hciAssignment.ui.screens.chatbot.components.MessageBubbleWithActions
import com.example.hciAssignment.ui.screens.chatbot.components.MessageBubbleWithContactAction
import com.example.hciAssignment.ui.screens.chatbot.components.MessageBubbleWithContactDetails
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.BottomSheetFrame
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.CheckoutBottomSheetFrame
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.SeatSelectionBottomSheetFrame
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.UserInfoBottomSheetFrame
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.ViewReservationsFrame
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatFrame(
    viewModel: ChatbotViewModel,
    state: ChatbotContract.State,
    snackBarState: SnackbarHostState,
    bottomSheetState: SheetState,
    listState: LazyListState
) {
    BackHandler(enabled = true) {
        if (!state.isWelcomeFrameVisible) {
            viewModel.setEvent(Event.OnBackButtonClicked)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.setEvent(Event.OnBackButtonClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                // Invisible (just for alignment)
                actions = {
                    Box(
                        modifier = Modifier
                            .size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Transparent
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    state = listState,
                    contentPadding = PaddingValues(top = 8.dp)
                ) {
                    items(state.messages) { message ->
                        when (message.messageType) {
                            MessageType.SELECT_PERFORMANCE -> {
                                state.availablePlays?.let {
                                    MessageBubbleWithActions(
                                        message = message,
                                        performances = it,
                                        onPerformanceSelected = { performance ->
                                            viewModel.setEvent(
                                                Event.OnPerformanceSelected(
                                                    performance
                                                )
                                            )
                                        },
                                        isEnabled = !state.isBotWriting
                                    )
                                }
                            }

                            MessageType.CONTACT -> {
                                MessageBubbleWithContactAction(
                                    message = message,
                                    onContactClick = {
                                        viewModel.setEvent(Event.OnContactClicked)
                                    },
                                    isEnabled = !state.isBotWriting,
                                )
                            }

                            MessageType.CONTACT_DETAILS -> {
                                state.contactDetails?.let {
                                    MessageBubbleWithContactDetails(
                                        message = message,
                                        contactDetails = it,
                                        isEnabled = !state.isBotWriting
                                    )
                                }
                            }

                            MessageType.OTHER -> {
                                MessageBubble(message = message)
                            }
                        }
                    }
                }

                ChatTextField(viewModel, state)
                ChatDialogs(viewModel, state)

                if (state.isBottomSheetVisible) {
                    ModalBottomSheet(
                        onDismissRequest = {},
                        sheetState = bottomSheetState,
                        properties = ModalBottomSheetProperties(
                            shouldDismissOnBackPress = false
                        )
                    ) {
                        BackHandler(enabled = true) {
                            when (state.visibleBottomSheetFrame) {
                                BottomSheetFrame.SEAT_SELECTION -> {
                                    viewModel.setEvent(Event.OnCancelBookingClicked)
                                }

                                BottomSheetFrame.USER_INFO -> {
                                    viewModel.setEvent(Event.OnBackToSeatSelectionClicked)
                                }

                                BottomSheetFrame.CHECKOUT -> {
                                    viewModel.setEvent(Event.OnBackToUserInfoClicked)
                                }

                                BottomSheetFrame.VIEW_TICKETS -> {
                                    viewModel.setEvent(Event.OnDismissBottomSheet)
                                }
                            }
                        }

                        when (state.visibleBottomSheetFrame) {
                            BottomSheetFrame.SEAT_SELECTION -> {
                                SeatSelectionBottomSheetFrame(state, viewModel)
                            }

                            BottomSheetFrame.USER_INFO -> {
                                UserInfoBottomSheetFrame(state, viewModel)
                            }

                            BottomSheetFrame.CHECKOUT -> {
                                CheckoutBottomSheetFrame(state, viewModel)
                            }

                            BottomSheetFrame.VIEW_TICKETS -> {
                                ViewReservationsFrame(state, viewModel, snackBarState)
                            }
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackBarState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 70.dp),
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
}

@Composable
fun ChatDialogs(
    viewModel: ChatbotViewModel,
    state: ChatbotContract.State
) {
    if (state.isCancelBookingAlertDialogVisible) {
        GenericAlertDialog(
            informativeText = stringResource(R.string.cancel_booking_alert_dialog_informative_text),
            negativeText = stringResource(R.string.cancel_booking_alert_dialog_negative_text),
            positiveText = stringResource(R.string.cancel_booking_alert_dialog_positive_text),
            onConfirm = { viewModel.setEvent(Event.CancelBookingAlertDialogEvent.OnPositiveClicked) },
            onDismiss = { viewModel.setEvent(Event.CancelBookingAlertDialogEvent.OnDismissed) },
            onCancel = { viewModel.setEvent(Event.CancelBookingAlertDialogEvent.OnNegativeClicked) }
        )
    }

    if (state.isConfirmBookingAlertDialogVisible) {
        GenericAlertDialog(
            informativeText = stringResource(R.string.confirm_booking_alert_dialog_informative_text),
            negativeText = stringResource(R.string.confirm_booking_alert_dialog_negative_text),
            positiveText = stringResource(R.string.confirm_booking_alert_dialog_positive_text),
            onConfirm = { viewModel.setEvent(Event.ConfirmBookingAlertDialogEvent.OnPositiveClicked) },
            onDismiss = { viewModel.setEvent(Event.ConfirmBookingAlertDialogEvent.OnDismissed) },
            onCancel = { viewModel.setEvent(Event.ConfirmBookingAlertDialogEvent.OnNegativeClicked) }
        )
    }

    if (state.isCancelTicketAlertDialogVisible) {
        GenericAlertDialog(
            informativeText = stringResource(R.string.cancel_ticket_alert_dialog_informative_text),
            negativeText = stringResource(R.string.cancel_ticket_alert_dialog_negative_text),
            positiveText = stringResource(R.string.cancel_ticket_alert_dialog_positive_text),
            onConfirm = { viewModel.setEvent(Event.CancelTicketAlertDialogEvent.OnPositiveClicked) },
            onDismiss = { viewModel.setEvent(Event.CancelTicketAlertDialogEvent.OnDismissed) },
            onCancel = { viewModel.setEvent(Event.CancelTicketAlertDialogEvent.OnNegativeClicked) }
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

@Composable
fun ChatTextField(
    viewModel: ChatbotViewModel,
    state: ChatbotContract.State
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .navigationBarsPadding()
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = state.text,
            onValueChange = {
                viewModel.setEvent(Event.TextEvent.OnTextValueChange(it))
            },
            modifier = Modifier
                .weight(1f)
                .padding(2.dp),
            maxLines = 4,
            placeholder = {
                Text(text = stringResource(R.string.chatbot_type_your_text_message))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                IconButton(
                    enabled = state.isSentButtonEnabled,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.setEvent(Event.TextEvent.OnMessageSent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null
                    )
                }
            }
        )
    }
}
