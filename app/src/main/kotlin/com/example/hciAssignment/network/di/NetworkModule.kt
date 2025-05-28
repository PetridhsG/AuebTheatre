package com.example.hciAssignment.network.di

import android.util.Log
import com.example.hciAssignment.BuildConfig
import com.example.hciAssignment.network.api.ApiClient
import com.example.hciAssignment.network.api.ApiClientImpl
import com.example.hciAssignment.network.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

val networkModule = module {

    single {
        val networkJson = Json { ignoreUnknownKeys = true }
        networkJson.asConverterFactory("application/json".toMediaType())
    }

    single {
        val httpLoggingInterceptor = provideHttpLoggingInterceptor()
        val authInterceptor = provideAuthInterceptor()

        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Converter.Factory>())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

    single<ApiClient> {
        ApiClientImpl(get())
    }
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

fun provideAuthInterceptor(): Interceptor {
    val apiKey = BuildConfig.OPENAI_API_KEY
    Log.i("AuthInterceptor", "API Key: $apiKey")
    return Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url.toString()

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .url(originalUrl)
            .build()

        chain.proceed(authenticatedRequest)
    }
}
