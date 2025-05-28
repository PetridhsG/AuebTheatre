package com.example.hciAssignment.ui.screens.chatbot.model

import com.example.hciAssignment.domain.model.Message
import com.example.hciAssignment.domain.model.Performance
import com.example.hciAssignment.domain.model.Seat
import com.example.hciAssignment.domain.model.Ticket
import com.example.hciAssignment.ui.screens.chatbot.frames.bottomsheetframes.BottomSheetFrame
import com.example.hciAssignment.ui.screens.chatbot.utils.ContactDetails
import com.example.hciAssignment.ui.screens.mvi.BaseEffect
import com.example.hciAssignment.ui.screens.mvi.BaseEvent
import com.example.hciAssignment.ui.screens.mvi.BaseState
import kotlinx.coroutines.Job

interface ChatbotContract {

    sealed class Event : BaseEvent {
        data object Init : Event()

        // Navigation events
        data object OnInfoButtonClicked : Event()
        data object OnTicketsButtonClicked : Event()
        data object OnGoToChatButtonClicked : Event()
        data object OnBackButtonClicked : Event()

        // Initial home frame events
        data object OnLearnAboutTheatreClicked : Event()
        data object OnLearnAboutPlaysClicked : Event()
        data object OnReserveSeatsClicked : Event()
        data object OnViewReservationsClicked : Event()
        data object OnContactWithRepresentativeClicked : Event()

        // Cancel booking alert dialog event
        data object OnCancelBookingClicked : Event()
        sealed class CancelBookingAlertDialogEvent : Event() {
            data object OnDismissed : Event()
            data object OnPositiveClicked : Event()
            data object OnNegativeClicked : Event()
        }

        // Confirm booking alert dialog event
        data object OnConfirmPaymentClicked : Event()
        sealed class ConfirmBookingAlertDialogEvent : Event() {
            data object OnDismissed : Event()
            data object OnPositiveClicked : Event()
            data object OnNegativeClicked : Event()
        }

        // Cancel ticket alert dialog event
        data class OnCancelTicketClicked(val ticket: Ticket) : Event()
        sealed class CancelTicketAlertDialogEvent : Event() {
            data object OnDismissed : Event()
            data object OnPositiveClicked : Event()
            data object OnNegativeClicked : Event()
        }

        // Bottom sheet navigation actions
        data object OnNextButtonClicked : Event()
        data object OnBackToSeatSelectionClicked : Event()
        data object OnProceedToPaymentClicked : Event()
        data object OnBackToUserInfoClicked : Event()

        // Text Inputs
        data class OnNameValueChange(val newValue: String) : Event()
        data class OnSurnameValueChange(val newValue: String) : Event()
        data class OnEmailValueChange(val newValue: String) : Event()
        data class OnTelephoneValueChange(val newValue: String) : Event()

        data class OnCardNumberValueChange(val newValue: String) : Event()
        data class OnExpiryDateValueChange(val newValue: String) : Event()
        data class OnCvvValueChange(val newValue: String) : Event()
        data class OnCardholderNameValueChange(val newValue: String) : Event()

        sealed class TextEvent : Event() {
            data class OnTextValueChange(val newText: String) : Event()
            data object OnMessageSent : Event()
        }

        // Other
        data object OnDismissBottomSheet : Event()
        data object OnContactClicked: Event()
        data class OnSeatSelected(val seat: Seat) : Event()
        data class OnPerformanceSelected(val performance: Performance) : Event()
    }

    data class State(
        // User Selections
        val selectedPerformance: Performance? = null,
        val selectedSeats: List<Seat> = emptyList(),

        // Bottom Sheet
        val visibleBottomSheetFrame: BottomSheetFrame = BottomSheetFrame.SEAT_SELECTION,
        val isBottomSheetVisible: Boolean = false,

        // Alert Dialogs
        val isCancelBookingAlertDialogVisible: Boolean = false,
        val isConfirmBookingAlertDialogVisible: Boolean = false,
        val isCancelTicketAlertDialogVisible: Boolean = false,

        // Text Inputs
        val text: String = "",
        val nameInput: String = "",
        val surnameInput: String = "",
        val emailInput: String = "",
        val phoneInput: String = "",
        val cardNumberInput: String = "",
        val expiryDateInput: String = "",
        val cvvInput: String = "",
        val cardholderNameInput: String = "",
        val errorLabelNameInput: String? = null,
        val errorLabelSurnameInput: String? = null,
        val errorLabelEmailInput: String? = null,
        val errorLabelTelephoneInput: String? = null,
        val errorLabelCardNumberInput: String? = null,
        val errorLabelExpiryDateInput: String? = null,
        val errorLabelCvvInput: String? = null,
        val errorLabelCardholderNameInput: String? = null,

        // User reservations
        val userTickets: List<Ticket> = emptyList(),
        val ticketToCancel: Ticket? = null,

        // Running jobs
        var botTypingJob: Job? = null,
        var intentJob: Job? = null,

        // Other
        val isWelcomeFrameVisible: Boolean = true,
        val messages: List<Message> = emptyList(),
        val isBotWriting: Boolean = false,
        val availablePlays: List<Performance>? = null,
        val unknownMessagesCount: Int = 0,
        val contactDetails: ContactDetails? = null
    ) : BaseState {
        val isSentButtonEnabled: Boolean
            get() = text.isNotBlank() && !isBotWriting

        val isNextButtonSeatSelectionEnabled: Boolean
            get() = selectedSeats.size in 1..4

        val isProceedToPaymentButtonEnabled: Boolean
            get() = nameInput.isNotBlank() &&
                    surnameInput.isNotBlank() &&
                    emailInput.isNotBlank() &&
                    phoneInput.isNotBlank() &&
                    errorLabelNameInput == null &&
                    errorLabelSurnameInput == null &&
                    errorLabelEmailInput == null &&
                    errorLabelTelephoneInput == null

        val isConfirmPaymentEnabled: Boolean
            get() = cardNumberInput.isNotBlank() &&
                    expiryDateInput.isNotBlank() &&
                    cvvInput.isNotBlank() &&
                    cardholderNameInput.isNotBlank() &&
                    errorLabelCardNumberInput == null &&
                    errorLabelExpiryDateInput == null &&
                    errorLabelCvvInput == null &&
                    errorLabelCardholderNameInput == null

    }

    sealed class Effect : BaseEffect {
        data object GoToInfoScreen : Effect()
        data object GoToTicketsScreen : Effect()
        data class ShowSnackbar(val message: String) : Effect()
        data object DismissSnackbar: Effect()
        data object DismissBottomSheet: Effect()
    }
}
