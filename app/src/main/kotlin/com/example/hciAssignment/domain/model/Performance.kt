package com.example.hciAssignment.domain.model

data class Performance(
    val title: String,
    val time: String,
    val seats: List<Seat>
)

fun Performance.performanceToString(): String = "$title at $time"
