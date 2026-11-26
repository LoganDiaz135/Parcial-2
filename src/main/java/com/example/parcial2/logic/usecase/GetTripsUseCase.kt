package com.example.parcial2.logic.usecase

import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.model.Trip

class GetTripsUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(): Result<List<Trip>> {
        return tripRepository.getTrips()
    }
}
