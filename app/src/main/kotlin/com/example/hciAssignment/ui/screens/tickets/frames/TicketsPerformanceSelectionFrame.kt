package com.example.hciAssignment.ui.screens.tickets.frames

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.model.performanceToString
import com.example.hciAssignment.ui.screens.tickets.TicketsViewModel
import com.example.hciAssignment.ui.screens.tickets.model.TicketsContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsPerformanceSelectionFrame(
    state: TicketsContract.State,
    viewModel: TicketsViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = stringResource(R.string.bottom_sheet_performance_selection_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically),
            )

            // Close Button
            IconButton(onClick = { viewModel.setEvent(TicketsContract.Event.OnCancelBookingClicked) }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.bottom_sheet_performance_selection_label),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.selectedPerformance?.performanceToString() ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.bottom_sheet_performance_selection_dropdown)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.availablePlays?.forEach { performance ->
                        DropdownMenuItem(
                            text = { Text(performance.performanceToString()) },
                            onClick = {
                                expanded = false
                                viewModel.setEvent(TicketsContract.Event.OnPerformanceSelected(performance))
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.setEvent(TicketsContract.Event.OnNextButtonPerformanceSelectionClicked)
                },
                enabled = state.isNextButtonPerformanceSelectionEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.bottom_sheet_performance_selection_next))
            }
        }
    }
}
