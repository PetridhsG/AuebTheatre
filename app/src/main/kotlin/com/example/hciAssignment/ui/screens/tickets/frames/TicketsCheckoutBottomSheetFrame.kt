package com.example.hciAssignment.ui.screens.tickets.frames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hciAssignment.R
import com.example.hciAssignment.ui.screens.tickets.TicketsViewModel
import com.example.hciAssignment.ui.screens.tickets.model.TicketsContract

@Composable
fun TicketsCheckoutBottomSheetFrame(
    state: TicketsContract.State,
    viewModel: TicketsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Button
            IconButton(
                onClick = {
                    viewModel.setEvent(TicketsContract.Event.OnBackToUserInfoClicked)
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }

            // Title Text
            Text(
                text = stringResource(R.string.bottom_sheet_checkout_frame_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Close Button
            IconButton(
                onClick = { viewModel.setEvent(TicketsContract.Event.OnCancelBookingClicked) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // Display Total Cost
            Text(
                text = stringResource(R.string.bottom_sheet_checkout_frame_total, state.selectedSeats.size * 20),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Cardholder Name
            OutlinedTextField(
                value = state.cardholderNameInput,
                onValueChange = {
                    viewModel.setEvent(TicketsContract.Event.OnCardholderNameValueChange(it))
                },
                label = { Text(stringResource(R.string.bottom_sheet_checkout_frame_label_cardholder_name)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_checkout_frame_placeholder_cardholder_name)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.errorLabelCardholderNameInput != null,
                maxLines = 1,
                supportingText = { Text(text = state.errorLabelCardholderNameInput.orEmpty()) }
            )

            // Card Number
            OutlinedTextField(
                value = state.cardNumberInput,
                onValueChange = {
                    viewModel.setEvent(TicketsContract.Event.OnCardNumberValueChange(it))
                },
                label = { Text(stringResource(R.string.bottom_sheet_checkout_frame_label_card_number)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_checkout_frame_placeholder_card_number)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.errorLabelCardNumberInput != null,
                maxLines = 1,
                supportingText = { Text(text = state.errorLabelCardNumberInput.orEmpty()) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Expiry Date
            OutlinedTextField(
                value = state.expiryDateInput,
                onValueChange = {
                    viewModel.setEvent(TicketsContract.Event.OnExpiryDateValueChange(it))
                },
                label = { Text(stringResource(R.string.bottom_sheet_checkout_frame_label_expiry_date)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_checkout_frame_placeholder_expiry_date)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.errorLabelExpiryDateInput != null,
                maxLines = 1,
                supportingText = { Text(text = state.errorLabelExpiryDateInput.orEmpty()) }
            )

            // CVV
            OutlinedTextField(
                value = state.cvvInput,
                onValueChange = {
                    viewModel.setEvent(TicketsContract.Event.OnCvvValueChange(it))
                },
                label = { Text(stringResource(R.string.bottom_sheet_checkout_frame_label_cvv)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_checkout_frame_placeholder_cvv)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.errorLabelCvvInput != null,
                maxLines = 1,
                supportingText = { Text(text = state.errorLabelCvvInput.orEmpty()) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Confirm Payment Button
            Button(
                onClick = { viewModel.setEvent(TicketsContract.Event.OnConfirmPaymentClicked) },
                enabled = state.isConfirmPaymentEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.bottom_sheet_checkout_frame_confirm_payment))
            }
        }
    }
}
