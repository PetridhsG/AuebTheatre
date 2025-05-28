package com.example.hciAssignment

import android.app.Application
import com.example.hciAssignment.di.initializeKoin

class HCIAssignmentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(this@HCIAssignmentApplication)
    }
}
