package com.example.parcial2.data.dto

data class PlaceDto(
    val id: Int?,
    val tripId: Int?,
    val name: String?,
    val description: String?,
    val city: String?,
    val categoryId: Int?,
    val tentativeDate: String?,
    val category: CategoryDto?
)
