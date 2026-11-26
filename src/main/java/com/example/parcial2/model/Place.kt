package com.example.parcial2.model

data class Place(
    val id: String,
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String,
    val category: Category? = null
)
