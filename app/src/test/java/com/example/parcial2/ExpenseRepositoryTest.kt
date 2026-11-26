package com.example.parcial2

import com.example.parcial2.logic.repository.PlaceRepository
import com.example.parcial2.model.Place
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpenseRepositoryTest {

    private class FakePlaceRepository : PlaceRepository {
        val listaGastos = mutableListOf<Place>()

        override suspend fun createPlace(
            tripId: String,
            name: String,
            description: String,
            city: String,
            categoryId: Int,
            tentativeDate: String,
            amount: Double
        ): Result<Place> {
            val nuevoGasto = Place(
                id = (listaGastos.size + 1).toString(),
                tripId = tripId,
                name = name,
                description = description,
                city = city,
                categoryId = categoryId,
                tentativeDate = tentativeDate,
                amount = amount
            )
            listaGastos.add(nuevoGasto)
            return Result.success(nuevoGasto)
        }
    }

    @Test
    fun createPlace_persisteGastoAsociadoAlTripIdCorrectamente() = runBlocking {
        val repo = FakePlaceRepository()

        val result = repo.createPlace(
            tripId = "trip-99",
            name = "Boletos tren",
            description = "Traslado Roma - Venecia",
            city = "Roma",
            categoryId = 3,
            tentativeDate = "2026-06-14",
            amount = 80.0
        )

        assertTrue(result.isSuccess)
        val gasto = result.getOrNull()
        assertEquals("trip-99", gasto?.tripId)
        assertEquals("Boletos tren", gasto?.name)
        assertEquals(80.0, gasto?.amount ?: 0.0, 0.001)
        assertEquals(1, repo.listaGastos.size)
    }
}
