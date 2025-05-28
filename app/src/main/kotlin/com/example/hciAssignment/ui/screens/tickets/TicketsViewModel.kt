package com.example.hciAssignment.ui.screens.tickets

import androidx.lifecycle.viewModelScope
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.ChatbotInteractor
import com.example.hciAssignment.domain.model.Seat
import com.example.hciAssignment.resources.ResourceProvider
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
import com.example.hciAssignment.ui.screens.tickets.frames.TicketsBottomSheetFrame
import com.example.hciAssignment.ui.screens.tickets.model.TicketsContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class TicketsViewModel :
    BaseViewModel<TicketsContract.Event, TicketsContract.State, TicketsContract.Effect>() {

    override fun setInitialState(): TicketsContract.State = TicketsContract.State()

    private val chatbotInteractor: ChatbotInteractor by inject()
    private val resourceProvider: ResourceProvider by inject()

    override fun handleEvents(event: TicketsContract.Event) {
        when (event) {
            TicketsContract.Event.Init -> {
                viewModelScope.launch {
                    val userTickets = chatbotInteractor.getTickets()
                    setState {
                        copy(
                            userTickets = userTickets,
                        )
                    }
                }
            }

            TicketsContract.Event.GoBack -> {
                setEffect {
                    TicketsContract.Effect.GoBack
                }
            }

            // Bottom sheet frame navigation events
            TicketsContract.Event.OnNextButtonPerformanceSelectionClicked -> {
                bottomSheetFrameNavigate(TicketsBottomSheetFrame.SEAT_SELECTION)
            }

            TicketsContract.Event.OnBackToPerformanceSelectionClicked -> {
                setState {
                    copy(
                        selectedSeats = emptyList(),
                        selectedPerformance = null
                    )
                }
                bottomSheetFrameNavigate(TicketsBottomSheetFrame.SELECT_PERFORMANCE)
            }

            TicketsContract.Event.OnNextButtonSeatSelectionClicked -> {
                bottomSheetFrameNavigate(TicketsBottomSheetFrame.USER_INFO)
            }

            TicketsContract.Event.OnBackToSeatSelectionClicked -> {
                bottomSheetFrameNavigate(TicketsBottomSheetFrame.SEAT_SELECTION)
            }

            TicketsContract.Event.OnProceedToPaymentClicked -> {
                bottomSheetFrameNavigate(TicketsBottomSheetFrame.CHECKOUT)
            }

            TicketsContract.Event.OnBackToUserInfoClicked -> {
                bottomSheetFrameNavigate(TicketsBottomSheetFrame.USER_INFO)
            }

            // Seat selection
            is TicketsContract.Event.OnSeatSelected -> {
                selectSeats(event.seat)
            }

            // Cancel booking alert dialog events
            TicketsContract.Event.CancelBookingAlertDialogEvent.OnDismissed -> {
                hideCancelBookingAlertDialog()
            }

            TicketsContract.Event.CancelBookingAlertDialogEvent.OnNegativeClicked -> {
                hideCancelBookingAlertDialog()
            }

            TicketsContract.Event.CancelBookingAlertDialogEvent.OnPositiveClicked -> {
                hideCancelBookingAlertDialog()
                clearUserChoices()
                hideBottomSheet()
            }

            TicketsContract.Event.OnCancelBookingClicked -> {
                showCancelBookingAlertDialog()
            }

            // Confirm booking alert dialog events
            TicketsContract.Event.ConfirmBookingAlertDialogEvent.OnDismissed -> {
                hideConfirmBookingAlertDialog()
            }

            TicketsContract.Event.ConfirmBookingAlertDialogEvent.OnNegativeClicked -> {
                hideConfirmBookingAlertDialog()
            }

            TicketsContract.Event.ConfirmBookingAlertDialogEvent.OnPositiveClicked -> {
                bookTickets()
            }

            TicketsContract.Event.OnConfirmPaymentClicked -> {
                showConfirmBookingAlertDialog()
            }

            // Cancel ticket alert dialog events
            TicketsContract.Event.CancelTicketAlertDialogEvent.OnDismissed -> {
                hideCancelTicketAlertDialog()
            }

            TicketsContract.Event.CancelTicketAlertDialogEvent.OnNegativeClicked -> {
                hideCancelTicketAlertDialog()
            }

            TicketsContract.Event.CancelTicketAlertDialogEvent.OnPositiveClicked -> {
                dismissSnackbar()
                cancelTicket()
            }

            is TicketsContract.Event.OnCancelTicketClicked -> {
                showCancelTicketAlertDialog()
                setState { copy(ticketToCancel = event.ticket) }
            }

            // Input fields events
            is TicketsContract.Event.OnNameValueChange -> {
                handleFieldValidation(field = UserInput.NAME, value = event.newValue)
            }

            is TicketsContract.Event.OnSurnameValueChange -> {
                handleFieldValidation(field = UserInput.SURNAME, value = event.newValue)
            }

            is TicketsContract.Event.OnEmailValueChange -> {
                handleFieldValidation(field = UserInput.EMAIL, value = event.newValue)
            }

            is TicketsContract.Event.OnTelephoneValueChange -> {
                handleFieldValidation(field = UserInput.TELEPHONE, value = event.newValue)
            }

            is TicketsContract.Event.OnCardNumberValueChange -> {
                handleFieldValidation(field = UserInput.CARD_NUMBER, value = event.newValue)
            }

            is TicketsContract.Event.OnExpiryDateValueChange -> {
                handleFieldValidation(UserInput.EXPIRY_DATE, value = event.newValue)
            }

            is TicketsContract.Event.OnCvvValueChange -> {
                handleFieldValidation(field = UserInput.CVV, value = event.newValue)
            }

            is TicketsContract.Event.OnCardholderNameValueChange -> {
                handleFieldValidation(field = UserInput.CARDHOLDER_NAME, value = event.newValue)
            }

            TicketsContract.Event.OnDismissBottomSheet -> {
                dismissSnackbar()
                hideBottomSheet()
            }

            is TicketsContract.Event.OnPerformanceSelected -> {
                setState {
                    copy(
                        selectedPerformance = event.performance
                    )
                }
            }

            TicketsContract.Event.OnFabClicked -> {
                viewModelScope.launch {
                    val availablePlays = chatbotInteractor.getPerformances()
                    setState {
                        copy(
                            availablePlays = availablePlays
                        )
                    }
                }
                showBottomSheet()
            }
        }
    }

    // Other actions
    private fun selectSeats(seat: Seat) {
        val currentSelectedSeats = currentState.selectedSeats.toMutableList()
        if (currentSelectedSeats.contains(seat)) {
            currentSelectedSeats.remove(seat) // Deselect
        } else {
            currentSelectedSeats.add(seat) // Select
        }
        setState { copy(selectedSeats = currentSelectedSeats) }
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
                    TicketsContract.Effect.ShowSnackbar(resourceProvider.getString(R.string.snackbar_reservation_cancel_successful))
                }
            }
        }
    }

    private fun bookTickets() {
        viewModelScope.launch {
            currentState.selectedPerformance?.let {
                chatbotInteractor.bookTickets(
                    performance = currentState.selectedPerformance!!,
                    seats = currentState.selectedSeats,
                    name = currentState.nameInput + " " + currentState.surnameInput
                )
                val userTickets = chatbotInteractor.getTickets()
                setState {
                    copy(
                        userTickets = userTickets
                    )
                }
                clearUserChoices()
                hideConfirmBookingAlertDialog()
                hideBottomSheet()
                delay(100)
                setEffect {
                    TicketsContract.Effect.ShowSnackbar(resourceProvider.getString(R.string.snackbar_reservation_successful))
                }
            }
        }
    }

    private fun hideBottomSheet() {
        setEffect {
            TicketsContract.Effect.OnDismissBottomSheet
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

    private fun bottomSheetFrameNavigate(bottomSheetFrame: TicketsBottomSheetFrame) {
        setState { copy(visibleBottomSheetFrame = bottomSheetFrame) }
    }

    private fun clearUserChoices() {
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
                visibleBottomSheetFrame = TicketsBottomSheetFrame.SELECT_PERFORMANCE
            )
        }
    }

    private fun dismissSnackbar() {
        setEffect {
            TicketsContract.Effect.DismissSnackbar
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
