package com.example.parcial2.logic.usecase

import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.model.TripDetail

class GetTripDetailUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(id: String): Result<TripDetail> {
        return tripRepository.getTripDetail(id)
    }
}
