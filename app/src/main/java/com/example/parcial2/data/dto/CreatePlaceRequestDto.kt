package com.example.parcial2.data.dto

data class CreatePlaceRequestDto(
    val tripId: Int,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String
)
