package com.example.hciAssignment.ui.screens.chatbot

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hciAssignment.ui.screens.chatbot.frames.ChatFrame
import com.example.hciAssignment.ui.screens.chatbot.frames.HomeFrame
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract
import com.example.hciAssignment.ui.screens.chatbot.model.ChatbotContract.Event
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    viewModel: ChatbotViewModel = koinViewModel(),
    onInfoButtonClick: () -> Unit,
    onTicketsButtonClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val listState = remember { LazyListState() }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { sheetValue -> sheetValue != SheetValue.Hidden }
    )

    LaunchedEffect(state.messages.lastOrNull()?.message) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setEvent(
            Event.Init
        )
        viewModel.uiEffect.collect { uiEffect ->
            when (uiEffect) {
                ChatbotContract.Effect.GoToInfoScreen -> {
                    onInfoButtonClick()
                }

                ChatbotContract.Effect.GoToTicketsScreen -> {
                    onTicketsButtonClick()
                }

                is ChatbotContract.Effect.ShowSnackbar -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(uiEffect.message)
                    }
                }

                ChatbotContract.Effect.DismissSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }

                ChatbotContract.Effect.DismissBottomSheet -> {
                    bottomSheetState.hide()
                }
            }
        }
    }

    if (state.isWelcomeFrameVisible) {
        HomeFrame(viewModel)
    } else {
        ChatFrame(
            viewModel = viewModel,
            state = state,
            snackBarState = snackbarHostState,
            bottomSheetState = bottomSheetState,
            listState = listState,
        )
    }
}
