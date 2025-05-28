package com.example.hciAssignment.domain.di

import com.example.hciAssignment.domain.ChatbotInteractor
import com.example.hciAssignment.domain.ChatbotInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<ChatbotInteractor> {
        ChatbotInteractorImpl(get(),get())
    }
}
