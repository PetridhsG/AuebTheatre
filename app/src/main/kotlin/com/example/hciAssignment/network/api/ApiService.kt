package com.example.hciAssignment.network.api

import com.example.hciAssignment.network.model.ApiResponse
import com.example.hciAssignment.network.model.ChatRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("chat/completions")
    suspend fun getChatResponse(
        @Body request: ChatRequest
    ): ApiResponse
}
