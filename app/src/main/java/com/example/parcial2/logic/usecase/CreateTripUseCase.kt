package com.example.parcial2.logic.usecase

import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.logic.validation.TripValidator
import com.example.parcial2.model.Trip

class CreateTripUseCase(
    private val tripRepository: TripRepository,
    private val tripValidator: TripValidator = TripValidator()
) {
    suspend operator fun invoke(
        name: String,
        destination: String,
        startDate: String,
        endDate: String
    ): Result<Trip> {
        val validation = tripValidator.validateTrip(name, destination, startDate, endDate)
        if (!validation.isValid) {
            return Result.failure(IllegalArgumentException(validation.errorMessage))
        }
        return tripRepository.createTrip(name, destination, startDate, endDate)
    }
}
