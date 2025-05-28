@file:Suppress("UnstableApiUsage")

import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.hciAssignment"
    compileSdk = 35

    // Load API key and base URL from local.properties
    val localProperties = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            load(FileInputStream(file))
        }
    }

    val apiKey = localProperties["API_KEY"] ?: ""
    val baseUrl = localProperties["BASE_URL"] ?: ""
    val model = localProperties["MODEL"] ?: ""
    val temperature = localProperties["TEMPERATURE"]?.toString()?.toDoubleOrNull() ?: 0.0

    defaultConfig {
        applicationId = "com.example.hciAssignment"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Securely include API key and base URL into BuildConfig
        buildConfigField("String", "OPENAI_API_KEY", "\"$apiKey\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "MODEL", "\"$model\"")
        buildConfigField("double", "TEMPERATURE", temperature.toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.compose)

    // Koin (Dependency Injection)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging)

    // Kotlinx Serialization
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlinx.serialization.converter)
}
