package com.example.hciAssignment.ui.screens.chatbot

import androidx.lifecycle.viewModelScope
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.ChatbotInteractor
import com.example.hciAssignment.domain.model.BotResponse
import com.example.hciAssignment.domain.model.ChatbotIntent
import com.example.hciAssignment.domain.model.Message
import com.example.hciAssignment.domain.model.MessageType
import com.example.hciAssignment.domain.model.Seat
import com.example.hciAssignment.domain.model.performanceToString
import com.example.hciAssignment.resources.ResourceProvider
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.BottomSheetFrame
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract
import com.example.hciAssignment.ui.screens.chatbot.utils.ContactDetails
import com.example.hciAssignment.ui.screens.chatbot.utils.UserInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateCardNumberInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateCardholderNameInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateCvvInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateEmailInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateExpiryDateInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateNameInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateSurnameInput
import com.example.hciAssignment.ui.screens.chatbot.utils.validateTelephoneInput
import com.example.hciAssignment.ui.screens.mvi.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatbotViewModel(
    private val resourceProvider: ResourceProvider,
    private val chatbotInteractor: ChatbotInteractor
) :
    BaseViewModel<ChatbotContract.Event, ChatbotContract.State, ChatbotContract.Effect>() {

    override fun setInitialState(): ChatbotContract.State = ChatbotContract.State()

    override fun handleEvents(event: ChatbotContract.Event) {
        when (event) {
            ChatbotContract.Event.Init -> {

            }

            // Frame events
            ChatbotContract.Event.OnBackButtonClicked -> {
                clearMessages()
                clearUserChoices()
                showWelcomeFrame()
                dismissSnackbar()
            }

            ChatbotContract.Event.OnGoToChatButtonClicked -> {
                clearMessages()
                hideWelcomeFrame()
            }

            // Bottom sheet frame navigation events
            ChatbotContract.Event.OnNextButtonClicked -> {
                bottomSheetFrameNavigate(BottomSheetFrame.USER_INFO)
            }

            ChatbotContract.Event.OnBackToSeatSelectionClicked -> {
                bottomSheetFrameNavigate(BottomSheetFrame.SEAT_SELECTION)
            }

            ChatbotContract.Event.OnProceedToPaymentClicked -> {
                bottomSheetFrameNavigate(BottomSheetFrame.CHECKOUT)
            }

            ChatbotContract.Event.OnBackToUserInfoClicked -> {
                bottomSheetFrameNavigate(BottomSheetFrame.USER_INFO)
            }

            // Chat events
            is ChatbotContract.Event.TextEvent.OnTextValueChange -> {
                updateText(event.newText)
            }

            ChatbotContract.Event.TextEvent.OnMessageSent -> {
                addTextToChat(currentState.text)
            }

            // Initial chat actions
            ChatbotContract.Event.OnLearnAboutTheatreClicked -> {
                clearUserChoices()
                clearMessages()
                addInitialTextToChat(resourceProvider.getString(R.string.cnatbot_learn_about_theatre_message))
            }

            ChatbotContract.Event.OnLearnAboutPlaysClicked -> {
                clearUserChoices()
                clearMessages()
                addInitialTextToChat(resourceProvider.getString(R.string.cnatbot_learn_about_plays_message))
            }

            ChatbotContract.Event.OnReserveSeatsClicked -> {
                clearUserChoices()
                clearMessages()
                addInitialTextToChat(resourceProvider.getString(R.string.cnatbot_book_tickets_message))
            }

            ChatbotContract.Event.OnViewReservationsClicked -> {
                clearUserChoices()
                clearMessages()
                addInitialTextToChat(resourceProvider.getString(R.string.cnatbot_check_tickets_message))
            }

            ChatbotContract.Event.OnContactWithRepresentativeClicked -> {
                clearUserChoices()
                clearMessages()
                addInitialTextToChat(resourceProvider.getString(R.string.cnatbot_contact_message))
            }

            // Seat selection
            is ChatbotContract.Event.OnSeatSelected -> {
                selectSeats(event.seat)
            }

            // Cancel booking alert dialog events
            ChatbotContract.Event.CancelBookingAlertDialogEvent.OnDismissed -> {
                hideCancelBookingAlertDialog()
            }

            ChatbotContract.Event.CancelBookingAlertDialogEvent.OnNegativeClicked -> {
                hideCancelBookingAlertDialog()
            }

            ChatbotContract.Event.CancelBookingAlertDialogEvent.OnPositiveClicked -> {
                setState {
                    copy(
                        messages = messages + Message(
                            message = resourceProvider.getString(R.string.chatbot_response_booking_cancelation_message),
                            isFromUser = false
                        )
                    )
                }
                hideCancelBookingAlertDialog()
                clearUserChoices()
                hideBottomSheet()
            }

            ChatbotContract.Event.OnCancelBookingClicked -> {
                showCancelBookingAlertDialog()
            }

            // Confirm booking alert dialog events
            ChatbotContract.Event.ConfirmBookingAlertDialogEvent.OnDismissed -> {
                hideConfirmBookingAlertDialog()
            }

            ChatbotContract.Event.ConfirmBookingAlertDialogEvent.OnNegativeClicked -> {
                hideConfirmBookingAlertDialog()
            }

            ChatbotContract.Event.ConfirmBookingAlertDialogEvent.OnPositiveClicked -> {
                setState {
                    copy(
                        messages = messages + Message(
                            message = resourceProvider.getString(R.string.chatbot_response_booking_confirmation_message),
                            isFromUser = false
                        )
                    )
                }
                bookTickets()
            }

            ChatbotContract.Event.OnConfirmPaymentClicked -> {
                showConfirmBookingAlertDialog()
            }

            // Cancel ticket alert dialog events
            ChatbotContract.Event.CancelTicketAlertDialogEvent.OnDismissed -> {
                hideCancelTicketAlertDialog()
            }

            ChatbotContract.Event.CancelTicketAlertDialogEvent.OnNegativeClicked -> {
                hideCancelTicketAlertDialog()
            }

            ChatbotContract.Event.CancelTicketAlertDialogEvent.OnPositiveClicked -> {
                dismissSnackbar()
                cancelTicket()
            }

            is ChatbotContract.Event.OnCancelTicketClicked -> {
                showCancelTicketAlertDialog()
                setState { copy(ticketToCancel = event.ticket) }
            }

            // Input fields events
            is ChatbotContract.Event.OnNameValueChange -> {
                handleFieldValidation(field = UserInput.NAME, value = event.newValue)
            }

            is ChatbotContract.Event.OnSurnameValueChange -> {
                handleFieldValidation(field = UserInput.SURNAME, value = event.newValue)
            }

            is ChatbotContract.Event.OnEmailValueChange -> {
                handleFieldValidation(field = UserInput.EMAIL, value = event.newValue)
            }

            is ChatbotContract.Event.OnTelephoneValueChange -> {
                handleFieldValidation(field = UserInput.TELEPHONE, value = event.newValue)
            }

            is ChatbotContract.Event.OnCardNumberValueChange -> {
                handleFieldValidation(field = UserInput.CARD_NUMBER, value = event.newValue)
            }

            is ChatbotContract.Event.OnExpiryDateValueChange -> {
                handleFieldValidation(UserInput.EXPIRY_DATE, value = event.newValue)
            }

            is ChatbotContract.Event.OnCvvValueChange -> {
                handleFieldValidation(field = UserInput.CVV, value = event.newValue)
            }

            is ChatbotContract.Event.OnCardholderNameValueChange -> {
                handleFieldValidation(field = UserInput.CARDHOLDER_NAME, value = event.newValue)
            }

            // Effects
            ChatbotContract.Event.OnInfoButtonClicked -> {
                setEffect {
                    ChatbotContract.Effect.GoToInfoScreen
                }
            }

            ChatbotContract.Event.OnTicketsButtonClicked -> {
                setEffect {
                    ChatbotContract.Effect.GoToTicketsScreen
                }
            }

            ChatbotContract.Event.OnDismissBottomSheet -> {
                dismissSnackbar()
                hideBottomSheet()
            }

            // Message action buttons
            is ChatbotContract.Event.OnPerformanceSelected -> {
                addTextToChat(event.performance.performanceToString())
            }

            ChatbotContract.Event.OnContactClicked -> {
                handleOnContactClicked()
            }
        }
    }

    // Theatre actions
    private fun selectSeats(seat: Seat) {
        val currentSelectedSeats = currentState.selectedSeats.toMutableList()
        if (currentSelectedSeats.contains(seat)) {
            currentSelectedSeats.remove(seat) // Deselect
        } else {
            currentSelectedSeats.add(seat) // Select
        }

        setState { copy(selectedSeats = currentSelectedSeats) }
    }

    private fun bookTickets() {
        viewModelScope.launch {
            currentState.selectedPerformance?.let {
                chatbotInteractor.bookTickets(
                    performance = currentState.selectedPerformance!!,
                    seats = currentState.selectedSeats,
                    name = currentState.nameInput + " " + currentState.surnameInput
                )
                clearUserChoices()
                hideConfirmBookingAlertDialog()
                hideBottomSheet()
                delay(100)

                setEffect {
                    ChatbotContract.Effect.ShowSnackbar(resourceProvider.getString(R.string.snackbar_reservation_successful))
                }
            }
        }
    }

    private fun cancelTicket() {
        viewModelScope.launch {
            currentState.ticketToCancel?.let { ticket ->
                chatbotInteractor.cancelTicket(ticket)
                setState {
                    copy(
                        userTickets = userTickets.filterNot {
                            it.performance == ticket.performance &&
                                    it.time == ticket.time &&
                                    it.seat == ticket.seat &&
                                    it.name == ticket.name
                        },
                        ticketToCancel = null,
                    )
                }
                hideCancelTicketAlertDialog()
                setEffect {
                    ChatbotContract.Effect.ShowSnackbar(resourceProvider.getString(R.string.snackbar_reservation_cancel_successful))
                }
            }
        }
    }

    // CHAT ACTIONS
    private fun addInitialTextToChat(userText: String) {
        hideWelcomeFrame()
        addTextToChat(userText)
    }

    private fun addTextToChat(userText: String) {
        addUserTextToChat(userText)
        addBotTextToChat(userText)
    }

    private fun addUserTextToChat(text: String) {
        setState {
            copy(
                messages = messages + Message(message = text.trimEnd(), isFromUser = true),
                text = ""
            )
        }
    }

    private fun handleBotResponse(botResponse: BotResponse) {
        when (botResponse.intent) {

            ChatbotIntent.THEATRE_INFO -> {

            }

            ChatbotIntent.PLAYS_INFO -> {

            }

            ChatbotIntent.BOOKING -> {
                currentState.intentJob = viewModelScope.launch {
                    val performances = chatbotInteractor.getPerformances()
                    setState {
                        val updatedMessages = messages.toMutableList()

                        val lastIndex =
                            updatedMessages.indexOfLast { !it.isFromUser && !it.isTypingIndicator }
                        if (lastIndex != -1) {
                            val updatedMessage =
                                updatedMessages[lastIndex].copy(messageType = MessageType.SELECT_PERFORMANCE)
                            updatedMessages[lastIndex] = updatedMessage
                        }

                        copy(
                            availablePlays = performances,
                            messages = updatedMessages
                        )
                    }
                }
            }

            ChatbotIntent.SELECT_PERFORMANCE -> {

            }

            ChatbotIntent.PROCEED_TO_BOOKING -> {
                currentState.intentJob = viewModelScope.launch {
                    val selectedPerformance = chatbotInteractor.getSelectedPerformance()
                    if(selectedPerformance != null){
                        delay(1000)
                        showBottomSheet()
                        bottomSheetFrameNavigate(BottomSheetFrame.SEAT_SELECTION)
                    }
                    setState { copy(selectedPerformance = selectedPerformance) }
                }
            }

            ChatbotIntent.VIEW_TICKETS -> {
                currentState.intentJob = viewModelScope.launch {
                    delay(1000)
                    showBottomSheet()
                    bottomSheetFrameNavigate(BottomSheetFrame.VIEW_TICKETS)
                    val tickets = chatbotInteractor.getTickets()
                    setState { copy(userTickets = tickets) }
                }
            }

            ChatbotIntent.CONTACT -> {
                handleOnContactClicked()
            }

            ChatbotIntent.UNKNOWN -> {
                if (currentState.unknownMessagesCount > 2) {
                    setState {
                        val updatedMessages = messages.toMutableList()
                        val lastIndex =
                            updatedMessages.indexOfLast { !it.isFromUser && !it.isTypingIndicator }

                        if (lastIndex != -1) {
                            val updatedMessage =
                                updatedMessages[lastIndex].copy(messageType = MessageType.CONTACT)
                            updatedMessages[lastIndex] = updatedMessage
                        }

                        copy(messages = updatedMessages)
                    }
                }
            }
        }
    }

    private fun addBotTextToChat(userText: String) {
        currentState.botTypingJob?.cancel()

        currentState.botTypingJob = viewModelScope.launch {
            setState { copy(isBotWriting = true) } // Show typing indicator
            addTypingIndicatorMessage()

            val newMessages =
                currentState.messages + Message(message = userText, isFromUser = false)

            val chatbotResponse = chatbotInteractor.getChatbotResponse(newMessages)

            if (chatbotResponse.intent != ChatbotIntent.UNKNOWN) {
                setState { copy(unknownMessagesCount = 0) }
            } else {
                setState { copy(unknownMessagesCount = unknownMessagesCount + 1) }
            }

            val message = if (currentState.unknownMessagesCount < 3) chatbotResponse.responseMessage
            else resourceProvider.getString(R.string.chatbot_failed_to_understand_message)

            setState {
                copy(
                    messages = messages.dropLast(1) + Message(message = "", isFromUser = false)
                )
            }

            simulateTypingEffect(message)
            setState { copy(isBotWriting = false) } // Mark the bot as done typing
            handleBotResponse(chatbotResponse)
        }
    }

    // Text actions
    private fun updateText(newText: String) {
        setState { copy(text = newText) }
    }

    private suspend fun simulateTypingEffect(fullText: String) {
        var currentText = ""
        for (char in fullText) {
            delay(10)
            currentText += char
            setState {
                copy(
                    messages = messages.dropLast(1) + Message(
                        message = currentText,
                        isFromUser = false
                    )
                )
            }
        }
    }

    private fun addTypingIndicatorMessage() {
        setState {
            copy(
                messages = messages + Message(
                    message = "",
                    isFromUser = false,
                    isTypingIndicator = true
                )
            )
        }
    }

    // FRAMES ACTIONS
    private fun hideWelcomeFrame() {
        setState { copy(isWelcomeFrameVisible = false) }
    }

    private fun showWelcomeFrame() {
        setState { copy(isWelcomeFrameVisible = true) }
    }

    private fun hideBottomSheet() {
        setEffect {
            ChatbotContract.Effect.DismissBottomSheet
        }
        setState { copy(isBottomSheetVisible = false) }
    }

    private fun showBottomSheet() {
        setState { copy(isBottomSheetVisible = true) }
    }

    private fun hideCancelBookingAlertDialog() {
        setState { copy(isCancelBookingAlertDialogVisible = false) }
    }

    private fun showCancelBookingAlertDialog() {
        setState { copy(isCancelBookingAlertDialogVisible = true) }
    }

    private fun hideConfirmBookingAlertDialog() {
        setState { copy(isConfirmBookingAlertDialogVisible = false) }
    }

    private fun showCancelTicketAlertDialog() {
        setState { copy(isCancelTicketAlertDialogVisible = true) }
    }

    private fun hideCancelTicketAlertDialog() {
        setState { copy(isCancelTicketAlertDialogVisible = false) }
    }

    private fun showConfirmBookingAlertDialog() {
        setState { copy(isConfirmBookingAlertDialogVisible = true) }
    }

    private fun bottomSheetFrameNavigate(bottomSheetFrame: BottomSheetFrame) {
        setState { copy(visibleBottomSheetFrame = bottomSheetFrame) }
    }

    private fun clearUserChoices() {
        currentState.botTypingJob?.cancel()
        currentState.intentJob?.cancel()
        viewModelScope.launch {
            chatbotInteractor.removeSelectedPerformance()
        }
        setState {
            copy(
                text = "",
                nameInput = "",
                surnameInput = "",
                emailInput = "",
                phoneInput = "",
                cardNumberInput = "",
                expiryDateInput = "",
                cvvInput = "",
                cardholderNameInput = "",
                errorLabelNameInput = null,
                errorLabelSurnameInput = null,
                errorLabelEmailInput = null,
                errorLabelTelephoneInput = null,
                errorLabelCardNumberInput = null,
                errorLabelExpiryDateInput = null,
                errorLabelCvvInput = null,
                errorLabelCardholderNameInput = null,
                selectedPerformance = null,
                selectedSeats = emptyList(),
                isBottomSheetVisible = false,
                isBotWriting = false
            )
        }
    }

    private fun clearMessages() {
        setState {
            copy(
                messages = emptyList(),
                unknownMessagesCount = 0
            )
        }
    }

    private fun dismissSnackbar() {
        setEffect {
            ChatbotContract.Effect.DismissSnackbar
        }
    }

    private fun handleOnContactClicked() {
        setState {
            val updatedMessages = messages.toMutableList()

            val lastIndex =
                updatedMessages.indexOfLast { !it.isFromUser && !it.isTypingIndicator }

            if (lastIndex != -1) {
                val updatedMessage =
                    updatedMessages[lastIndex].copy(messageType = MessageType.CONTACT_DETAILS)
                updatedMessages[lastIndex] = updatedMessage
            }

            copy(
                contactDetails = ContactDetails(
                    phone = resourceProvider.getString(R.string.contact_phone_number),
                    email = resourceProvider.getString(R.string.contact_email_address),
                    website = resourceProvider.getString(R.string.contact_website)
                ),
                messages = updatedMessages
            )
        }
    }

    // Input texts actions
    private fun handleFieldValidation(
        field: UserInput,
        value: String
    ) {
        setState {
            when (field) {
                UserInput.NAME -> copy(
                    nameInput = value,
                    errorLabelNameInput = validateNameInput(value, resourceProvider)
                )

                UserInput.SURNAME -> copy(
                    surnameInput = value,
                    errorLabelSurnameInput = validateSurnameInput(value, resourceProvider)
                )

                UserInput.EMAIL -> copy(
                    emailInput = value,
                    errorLabelEmailInput = validateEmailInput(value, resourceProvider)
                )

                UserInput.TELEPHONE -> copy(
                    phoneInput = value,
                    errorLabelTelephoneInput = validateTelephoneInput(value, resourceProvider)
                )

                UserInput.CARD_NUMBER -> copy(
                    cardNumberInput = value,
                    errorLabelCardNumberInput = validateCardNumberInput(value, resourceProvider)
                )

                UserInput.EXPIRY_DATE -> copy(
                    expiryDateInput = value,
                    errorLabelExpiryDateInput = validateExpiryDateInput(value, resourceProvider)
                )

                UserInput.CVV -> copy(
                    cvvInput = value,
                    errorLabelCvvInput = validateCvvInput(value, resourceProvider)
                )

                UserInput.CARDHOLDER_NAME -> copy(
                    cardholderNameInput = value,
                    errorLabelCardholderNameInput = validateCardholderNameInput(
                        value,
                        resourceProvider
                    )
                )
            }
        }
    }
}
