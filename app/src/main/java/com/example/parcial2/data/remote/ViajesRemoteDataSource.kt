package com.example.parcial2.data.remote

import com.example.parcial2.data.api.ViajesApiService
import com.example.parcial2.data.dto.CategoryDto
import com.example.parcial2.data.dto.CreatePlaceRequestDto
import com.example.parcial2.data.dto.CreateTripRequestDto
import com.example.parcial2.data.dto.PlaceDto
import com.example.parcial2.data.dto.TripDetailDto
import com.example.parcial2.data.dto.TripDto

class ViajesRemoteDataSource(
    private val apiService: ViajesApiService
) {
    suspend fun getTrips(): List<TripDto> {
        val response = apiService.getTrips()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al consultar viajes: ${response.code()} ${response.message()}")
        }
    }

    suspend fun createTrip(request: CreateTripRequestDto): TripDto {
        val response = apiService.createTrip(request)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al crear el viaje: ${response.code()} ${response.message()}")
        }
    }

    suspend fun getTripDetail(id: Int): TripDetailDto {
        val response = apiService.getTripDetail(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al obtener detalle del viaje: ${response.code()} ${response.message()}")
        }
    }

    suspend fun getCategories(): List<CategoryDto> {
        val response = apiService.getCategories()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al consultar categorías: ${response.code()} ${response.message()}")
        }
    }

    suspend fun createPlace(request: CreatePlaceRequestDto): PlaceDto {
        val response = apiService.createPlace(request)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error al registrar lugar: ${response.code()} ${response.message()}")
        }
    }
}
