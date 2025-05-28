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
fun TicketsUserInfoBottomSheetFrame(
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.setEvent(TicketsContract.Event.OnBackToSeatSelectionClicked)
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }

            Text(
                text = stringResource(R.string.bottom_sheet_user_info_frame_enter_details_text),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            IconButton(
                onClick = {
                    viewModel.setEvent(TicketsContract.Event.OnCancelBookingClicked)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.bottom_sheet_user_info_frame_close_icon_content_description)
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
            OutlinedTextField(
                value = state.nameInput,
                onValueChange = { newName ->
                    viewModel.setEvent(TicketsContract.Event.OnNameValueChange(newName))
                },
                label = { Text(stringResource(R.string.bottom_sheet_user_info_frame_label_name)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_user_info_frame_placeholder_name)) },
                isError = state.errorLabelNameInput != null,
                maxLines = 1,
                supportingText = {
                    Text(text = state.errorLabelNameInput.orEmpty())
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.surnameInput,
                onValueChange = { newSurname ->
                    viewModel.setEvent(TicketsContract.Event.OnSurnameValueChange(newSurname))
                },
                label = { Text(stringResource(R.string.bottom_sheet_user_info_frame_label_surname)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_user_info_frame_placeholder_surname)) },
                isError = state.errorLabelSurnameInput != null,
                maxLines = 1,
                supportingText = {
                    Text(text = state.errorLabelSurnameInput.orEmpty())
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.emailInput,
                onValueChange = { newEmail ->
                    viewModel.setEvent(TicketsContract.Event.OnEmailValueChange(newEmail))
                },
                label = { Text(stringResource(R.string.bottom_sheet_user_info_frame_label_email)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_user_info_frame_placeholder_email)) },
                isError = state.errorLabelEmailInput != null,
                maxLines = 1,
                supportingText = {
                    Text(text = state.errorLabelEmailInput.orEmpty())
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.phoneInput,
                onValueChange = { newPhone ->
                    viewModel.setEvent(TicketsContract.Event.OnTelephoneValueChange(newPhone))
                },
                label = { Text(stringResource(R.string.bottom_sheet_user_info_frame_label_telephone)) },
                placeholder = { Text(stringResource(R.string.bottom_sheet_user_info_frame_placeholder_telephone)) },
                isError = state.errorLabelTelephoneInput != null,
                supportingText = {
                    Text(text = state.errorLabelTelephoneInput.orEmpty())
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    viewModel.setEvent(TicketsContract.Event.OnProceedToPaymentClicked)
                },
                enabled = state.isProceedToPaymentButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.bottom_sheet_user_info_frame_button_proceed_to_payment))
            }
        }
    }
}
