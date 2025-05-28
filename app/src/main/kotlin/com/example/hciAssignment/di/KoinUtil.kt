package com.example.hciAssignment.di

import com.example.hciAssignment.HCIAssignmentApplication
import com.example.hciAssignment.domain.di.domainModule
import com.example.hciAssignment.network.di.networkModule

import com.example.hciAssignment.resources.di.resourcesModule
import com.example.hciAssignment.ui.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initializeKoin(
    application: HCIAssignmentApplication
) {
    startKoin {
        androidLogger(
            Level.DEBUG
        )

        androidContext(application)

        modules(
            networkModule,
            domainModule,
            viewModelsModule,
            resourcesModule
        )
    }
}
