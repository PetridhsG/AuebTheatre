package com.example.hciAssignment.domain.model

data class Seat(
    val row: Char,
    val column: Int,
    var isAvailable: Boolean
)

fun Seat.seatToString(): String = "$row$column"
