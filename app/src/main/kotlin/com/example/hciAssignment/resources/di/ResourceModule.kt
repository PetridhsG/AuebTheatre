package com.example.hciAssignment.resources.di

import com.example.hciAssignment.resources.ResourceProvider
import com.example.hciAssignment.resources.ResourceProviderImpl
import org.koin.dsl.module

val resourcesModule = module {
    single<ResourceProvider> {
        ResourceProviderImpl(get())
    }
}
