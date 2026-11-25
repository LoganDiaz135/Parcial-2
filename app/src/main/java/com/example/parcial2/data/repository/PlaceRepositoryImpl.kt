package com.example.parcial2.data.repository

import com.example.parcial2.data.dto.CreatePlaceRequestDto
import com.example.parcial2.data.mapper.toDomain
import com.example.parcial2.data.remote.ViajesRemoteDataSource
import com.example.parcial2.logic.repository.PlaceRepository
import com.example.parcial2.model.Place

class PlaceRepositoryImpl(
    private val remoteDataSource: ViajesRemoteDataSource
) : PlaceRepository {

    override suspend fun createPlace(
        tripId: Int,
        name: String,
        description: String,
        city: String,
        categoryId: Int,
        tentativeDate: String
    ): Result<Place> {
        return try {
            val request = CreatePlaceRequestDto(
                tripId = tripId,
                name = name,
                description = description,
                city = city,
                categoryId = categoryId,
                tentativeDate = tentativeDate
            )
            val dto = remoteDataSource.createPlace(request)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
