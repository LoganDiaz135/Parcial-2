package com.example.parcial2.logic.repository

import com.example.parcial2.model.Trip
import com.example.parcial2.model.TripDetail

interface TripRepository {
    suspend fun getTrips(): Result<List<Trip>>
    suspend fun createTrip(name: String, destination: String, startDate: String, endDate: String): Result<Trip>
    suspend fun getTripDetail(id: String): Result<TripDetail>
}
