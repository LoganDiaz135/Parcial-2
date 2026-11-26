package com.example.parcial2.data.dto

data class TripDetailDto(
    val id: String?,
    val name: String?,
    val destination: String?,
    val startDate: String?,
    val endDate: String?,
    val places: List<PlaceDto>?,
    val placesCount: Int?
)
