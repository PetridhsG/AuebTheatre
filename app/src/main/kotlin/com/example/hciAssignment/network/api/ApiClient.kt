package com.example.hciAssignment.network.api

import android.util.Log
import com.example.hciAssignment.network.model.ChatRequest
import com.example.hciAssignment.network.model.ChatResponse
import kotlinx.serialization.json.Json

interface ApiClient {
    suspend fun getChatResponse(request: ChatRequest) : ChatResponse
}

class ApiClientImpl(private val apiService: ApiService) : ApiClient {
    override suspend fun getChatResponse(request: ChatRequest): ChatResponse {
         try {
            val apiResponse = apiService.getChatResponse(request)
            val rawContent = apiResponse.choices.first().message.content

            Log.d("RawContent", rawContent)

            val jsonRegex = """\{[^}]+\}""".toRegex()
            val jsonMatch = jsonRegex.find(rawContent)

            val fixedJson = jsonMatch?.value
                ?.replace(Regex("""(\bintent\b)"""), "\"intent\"")
                ?.replace(Regex("""(?<!")\b(THEATRE_INFO|PLAYS_INFO|BOOKING|SELECT_PERFORMANCE|PROCEED_TO_BOOKING|VIEW_TICKETS|CONTACT|UNKNOWN)\b(?!")"""), "\"$1\"")

            return if (fixedJson != null) {
                Json.decodeFromString<ChatResponse>(fixedJson)
            } else {
                Log.e("ApiClientImpl", "No valid JSON found")
                ChatResponse(intent = "UNKNOWN")
            }

        } catch (e: Exception) {
            Log.e("ApiClientImpl", "Error fetching chat response", e)
            throw e
        }
    }
}
