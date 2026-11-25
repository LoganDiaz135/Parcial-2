package com.example.parcial2.model

data class TripDetail(
    val id: Int,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val places: List<Place>,
    val placesCount: Int
)
