package com.example.parcial2.data.api

import com.example.parcial2.data.dto.CategoryDto
import com.example.parcial2.data.dto.CreatePlaceRequestDto
import com.example.parcial2.data.dto.CreateTripRequestDto
import com.example.parcial2.data.dto.PlaceDto
import com.example.parcial2.data.dto.TripDetailDto
import com.example.parcial2.data.dto.TripDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ViajesApiService {

    @GET("api/trips")
    suspend fun getTrips(): Response<List<TripDto>>

    @POST("api/trips")
    suspend fun createTrip(@Body request: CreateTripRequestDto): Response<TripDto>

    @GET("api/trips/{id}")
    suspend fun getTripDetail(@Path("id") id: String): Response<TripDetailDto>

    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @GET("api/places")
    suspend fun getPlaces(@Query("tripId") tripId: String? = null): Response<List<PlaceDto>>

    @POST("api/places")
    suspend fun createPlace(@Body request: CreatePlaceRequestDto): Response<PlaceDto>
}
