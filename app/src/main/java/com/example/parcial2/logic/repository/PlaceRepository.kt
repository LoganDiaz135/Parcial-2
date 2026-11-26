package com.example.parcial2.logic.repository

import com.example.parcial2.model.Place

interface PlaceRepository {
    suspend fun createPlace(
        tripId: String,
        name: String,
        description: String,
        city: String,
        categoryId: Int,
        tentativeDate: String
    ): Result<Place>
}
