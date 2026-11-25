package com.example.parcial2.data.repository

import com.example.parcial2.data.dto.CreateTripRequestDto
import com.example.parcial2.data.mapper.toDomain
import com.example.parcial2.data.remote.ViajesRemoteDataSource
import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.model.Trip
import com.example.parcial2.model.TripDetail

class TripRepositoryImpl(
    private val remoteDataSource: ViajesRemoteDataSource
) : TripRepository {

    override suspend fun getTrips(): Result<List<Trip>> {
        return try {
            val dtoList = remoteDataSource.getTrips()
            Result.success(dtoList.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTrip(
        name: String,
        destination: String,
        startDate: String,
        endDate: String
    ): Result<Trip> {
        return try {
            val request = CreateTripRequestDto(
                name = name,
                destination = destination,
                startDate = startDate,
                endDate = endDate
            )
            val dto = remoteDataSource.createTrip(request)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTripDetail(id: Int): Result<TripDetail> {
        return try {
            val dto = remoteDataSource.getTripDetail(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
