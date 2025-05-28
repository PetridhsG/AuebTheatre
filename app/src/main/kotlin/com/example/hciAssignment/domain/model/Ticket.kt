package com.example.hciAssignment.domain.model

data class Ticket(
    val performance: String,
    val date: String,
    val time: String,
    val room: String,
    val seat: String,
    val name: String
)
