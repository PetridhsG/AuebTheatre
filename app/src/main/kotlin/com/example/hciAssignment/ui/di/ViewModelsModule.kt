package com.example.hciAssignment.ui.di

import com.example.hciAssignment.ui.screens.chatbot.ChatbotViewModel
import com.example.hciAssignment.ui.screens.info.InfoViewModel
import com.example.hciAssignment.ui.screens.tickets.TicketsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { ChatbotViewModel(get(),get()) }
    viewModel { InfoViewModel() }
    viewModel { TicketsViewModel() }
}
