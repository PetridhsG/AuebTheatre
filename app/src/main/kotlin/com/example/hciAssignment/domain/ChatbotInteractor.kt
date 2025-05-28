package com.example.hciAssignment.domain

import android.util.Log
import com.example.hciAssignment.BuildConfig
import com.example.hciAssignment.R
import com.example.hciAssignment.domain.model.BotResponse
import com.example.hciAssignment.domain.model.ChatbotIntent
import com.example.hciAssignment.domain.model.Message
import com.example.hciAssignment.domain.model.Performance
import com.example.hciAssignment.domain.model.Seat
import com.example.hciAssignment.domain.model.Ticket
import com.example.hciAssignment.domain.model.seatToString
import com.example.hciAssignment.network.api.ApiClient
import com.example.hciAssignment.network.model.ChatMessage
import com.example.hciAssignment.network.model.ChatRequest
import com.example.hciAssignment.network.model.ChatResponse
import com.example.hciAssignment.resources.ResourceProvider

interface ChatbotInteractor {
    suspend fun bookTickets(
        performance: Performance,
        seats: List<Seat>,
        name: String
    )

    suspend fun getPerformances(): List<Performance>
    suspend fun getSelectedPerformance(): Performance?
    suspend fun removeSelectedPerformance()
    suspend fun getTickets(): List<Ticket>
    suspend fun cancelTicket(ticket: Ticket)
    suspend fun getChatbotResponse(messages: List<Message>): BotResponse
}

class ChatbotInteractorImpl(
    private val apiClient: ApiClient,
    private val resourceProvider: ResourceProvider
) : ChatbotInteractor {

    private var performances = generateInitialPerformances().toMutableList()
    private val userTickets = mutableListOf<Ticket>()
    private var selectedPerformance: Performance? = null

    override suspend fun bookTickets(
        performance: Performance,
        seats: List<Seat>,
        name: String
    ) {
        reserveSeats(performance, seats)
        seats.forEach { seat ->
            val ticket = Ticket(
                performance = performance.title,
                date = "25/07/2025",
                time = performance.time,
                room = if (performance.title == "Hamlet") "Antoniadou" else "Derigni",
                seat = seat.seatToString(),
                name = name
            )
            userTickets += ticket
        }
    }

    override suspend fun getPerformances(): List<Performance> = performances

    override suspend fun getSelectedPerformance(): Performance? {
        return selectedPerformance
    }

    override suspend fun removeSelectedPerformance() {
        selectedPerformance = null
    }

    override suspend fun getTickets(): List<Ticket> = userTickets

    override suspend fun cancelTicket(ticket: Ticket) {
        userTickets.removeIf {
            it.performance == ticket.performance &&
                    it.time == ticket.time &&
                    it.seat == ticket.seat &&
                    it.name == ticket.name
        }

        val performanceIndex = performances.indexOfFirst {
            it.title == ticket.performance && it.time == ticket.time
        }

        if (performanceIndex != -1) {
            val originalPerformance = performances[performanceIndex]
            val updatedSeats = originalPerformance.seats.map { seat ->
                if (seat.seatToString() == ticket.seat) seat.copy(isAvailable = true)
                else seat
            }
            performances[performanceIndex] = originalPerformance.copy(seats = updatedSeats)
        }
    }

    override suspend fun getChatbotResponse(messages: List<Message>): BotResponse {
        return try {
            val matchedPerformance = matchPerformanceFromMessage(messages)
            matchedPerformance?.let { selectedPerformance = it }

            val systemMessage = resourceProvider.getString(R.string.system_message)
            val messagesToSend = buildMessagesForAPI(messages, systemMessage)
            val apiResponse = getApiResponse(messagesToSend)

            val intent = ChatbotIntent.fromRaw(apiResponse.intent)
            generateBotResponse(intent)
        } catch (e: Exception) {
            Log.e("ChatbotInteractor", "API error: ${e.localizedMessage}", e)
            BotResponse(
                intent = ChatbotIntent.UNKNOWN,
                responseMessage = resourceProvider.getString(R.string.chatbot_failed_response)
            )
        }
    }

    private fun reserveSeats(performance: Performance, selectedSeats: List<Seat>) {
        val match = performances.find {
            it.title == performance.title && it.time == performance.time
        }

        match?.seats?.forEach { seat ->
            if (selectedSeats.any { it.row == seat.row && it.column == seat.column }) {
                seat.isAvailable = false
            }
        }
    }

    private fun generateSeats(): List<Seat> {
        val seats = mutableListOf<Seat>()
        val rows = 'A'..'H'
        val columns = 1..8
        for (row in rows) {
            for (col in columns) {
                val isAvailable = (0..100).random() > 20
                seats.add(Seat(row, col, isAvailable))
            }
        }
        return seats
    }

    private fun generateInitialPerformances(): List<Performance> {
        return listOf(
            Performance("Hamlet", "18:00", generateSeats()),
            Performance("Hamlet", "21:00", generateSeats()),
            Performance("Oedipus Rex", "18:00", generateSeats()),
            Performance("Oedipus Rex", "21:00", generateSeats())
        )
    }

    private fun matchPerformanceFromMessage(messages: List<Message>): Performance? {
        val userText = messages.lastOrNull { it.isFromUser }?.message?.lowercase()?.trim() ?: ""
        return performances.firstOrNull { performance ->
            userText.contains(performance.title.lowercase()) &&
                    userText.contains(performance.time)
        }
    }

    private fun buildMessagesForAPI(
        messages: List<Message>,
        systemMessage: String
    ): List<ChatMessage> {
        return messages.fold(
            listOf(
                ChatMessage(
                    role = "system",
                    content = systemMessage
                )
            )
        ) { acc, message ->
            val role = if (message.isFromUser) "user" else "assistant"
            val content = message.message
            acc + ChatMessage(role = role, content = content)
        }
    }

    private suspend fun getApiResponse(messagesToSend: List<ChatMessage>): ChatResponse {
        return apiClient.getChatResponse(
            ChatRequest(
                model = BuildConfig.MODEL,
                messages = messagesToSend,
                temperature = BuildConfig.TEMPERATURE
            )
        )
    }

    private fun generateBotResponse(intent: ChatbotIntent): BotResponse {
        val performance = selectedPerformance
        val name = performance?.title ?: "a performance"
        val time = performance?.time ?: "an unknown time"
        return when (intent) {
            ChatbotIntent.THEATRE_INFO -> BotResponse(
                intent = ChatbotIntent.THEATRE_INFO,
                responseMessage = resourceProvider.getString(R.string.theatre_info)
            )

            ChatbotIntent.PLAYS_INFO -> BotResponse(
                intent = ChatbotIntent.PLAYS_INFO,
                responseMessage = resourceProvider.getString(R.string.plays_info)
            )

            ChatbotIntent.BOOKING -> BotResponse(
                intent = ChatbotIntent.BOOKING,
                responseMessage = resourceProvider.getString(R.string.booking_response)
            )

            ChatbotIntent.SELECT_PERFORMANCE -> BotResponse(
                intent = ChatbotIntent.SELECT_PERFORMANCE,
                responseMessage = resourceProvider.getString(
                    R.string.select_performance_response,
                    "$name at $time"
                )
            )

            ChatbotIntent.PROCEED_TO_BOOKING -> BotResponse(
                intent = ChatbotIntent.PROCEED_TO_BOOKING,
                responseMessage = if (selectedPerformance != null) resourceProvider.getString(R.string.proceed_to_booking_response)
                else resourceProvider.getString(R.string.performance_not_selected)
            )

            ChatbotIntent.VIEW_TICKETS -> BotResponse(
                intent = ChatbotIntent.VIEW_TICKETS,
                responseMessage = resourceProvider.getString(R.string.view_tickets_response)
            )

            ChatbotIntent.CONTACT -> BotResponse(
                intent = ChatbotIntent.CONTACT,
                responseMessage = resourceProvider.getString(R.string.contact_response)
            )

            ChatbotIntent.UNKNOWN -> BotResponse(
                intent = ChatbotIntent.UNKNOWN,
                responseMessage = resourceProvider.getString(R.string.unknown_response)
            )
        }
    }
}
