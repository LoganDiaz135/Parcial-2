package com.example.parcial2.data.mapper

import com.example.parcial2.data.dto.CategoryDto
import com.example.parcial2.data.dto.PlaceDto
import com.example.parcial2.data.dto.TripDetailDto
import com.example.parcial2.data.dto.TripDto
import com.example.parcial2.model.Category
import com.example.parcial2.model.Place
import com.example.parcial2.model.Trip
import com.example.parcial2.model.TripDetail

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id ?: 0,
        name = name.orEmpty(),
        description = description.orEmpty()
    )
}

fun TripDto.toDomain(): Trip {
    return Trip(
        id = id.orEmpty(),
        name = name.orEmpty(),
        destination = destination.orEmpty(),
        startDate = startDate.orEmpty(),
        endDate = endDate.orEmpty()
    )
}

fun PlaceDto.toDomain(): Place {
    return Place(
        id = id.orEmpty(),
        tripId = tripId.orEmpty(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        city = city.orEmpty(),
        categoryId = categoryId ?: 0,
        tentativeDate = tentativeDate.orEmpty(),
        category = category?.toDomain()
    )
}

fun TripDetailDto.toDomain(): TripDetail {
    return TripDetail(
        id = id.orEmpty(),
        name = name.orEmpty(),
        destination = destination.orEmpty(),
        startDate = startDate.orEmpty(),
        endDate = endDate.orEmpty(),
        places = places?.map { it.toDomain() } ?: emptyList(),
        placesCount = placesCount ?: (places?.size ?: 0)
    )
}
