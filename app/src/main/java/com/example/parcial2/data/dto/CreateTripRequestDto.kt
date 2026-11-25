package com.example.parcial2.data.dto

data class CreateTripRequestDto(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)
