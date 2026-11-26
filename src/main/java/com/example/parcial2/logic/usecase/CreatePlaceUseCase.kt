package com.example.parcial2.logic.usecase

import com.example.parcial2.logic.repository.PlaceRepository
import com.example.parcial2.logic.validation.TripValidator
import com.example.parcial2.model.Place

class CreatePlaceUseCase(
    private val placeRepository: PlaceRepository,
    private val tripValidator: TripValidator = TripValidator()
) {
    suspend operator fun invoke(
        tripId: String,
        name: String,
        description: String,
        city: String,
        categoryId: Int,
        tentativeDate: String,
        tripStartDate: String,
        tripEndDate: String
    ): Result<Place> {
        val validation = tripValidator.validatePlace(
            name = name,
            description = description,
            city = city,
            categoryId = categoryId,
            tentativeDate = tentativeDate,
            tripStartDate = tripStartDate,
            tripEndDate = tripEndDate
        )
        if (!validation.isValid) {
            return Result.failure(IllegalArgumentException(validation.errorMessage))
        }
        return placeRepository.createPlace(
            tripId = tripId,
            name = name,
            description = description,
            city = city,
            categoryId = categoryId,
            tentativeDate = tentativeDate
        )
    }
}
