package com.example.parcial2

import com.example.parcial2.logic.repository.PlaceRepository
import com.example.parcial2.logic.usecase.CreatePlaceUseCase
import com.example.parcial2.model.Place
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreatePlaceUseCaseTest {

    private class FakePlaceRepository : PlaceRepository {
        val lugaresCreados = mutableListOf<Place>()

        override suspend fun createPlace(
            tripId: String,
            name: String,
            description: String,
            city: String,
            categoryId: Int,
            tentativeDate: String,
            amount: Double
        ): Result<Place> {
            val nuevo = Place(
                id = "101",
                tripId = tripId,
                name = name,
                description = description,
                city = city,
                categoryId = categoryId,
                tentativeDate = tentativeDate,
                amount = amount
            )
            lugaresCreados.add(nuevo)
            return Result.success(nuevo)
        }
    }

    @Test
    fun invoke_conLugarValido_creaLugarExitosamente() = runBlocking {
        val fakeRepo = FakePlaceRepository()
        val useCase = CreatePlaceUseCase(fakeRepo)

        val result = useCase(
            tripId = "1",
            name = "Coliseo Romano",
            description = "Anfiteatro histórico",
            city = "Roma",
            categoryId = 1,
            tentativeDate = "2026-06-15",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 25.0
        )

        assertTrue(result.isSuccess)
        assertEquals("Coliseo Romano", result.getOrNull()?.name)
        assertEquals(1, fakeRepo.lugaresCreados.size)
    }

    @Test
    fun invoke_conFechaFueraDeRango_retornaError() = runBlocking {
        val fakeRepo = FakePlaceRepository()
        val useCase = CreatePlaceUseCase(fakeRepo)

        val result = useCase(
            tripId = "1",
            name = "Coliseo Romano",
            description = "Anfiteatro histórico",
            city = "Roma",
            categoryId = 1,
            tentativeDate = "2026-07-01",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 25.0
        )

        assertTrue(result.isFailure)
        assertEquals(0, fakeRepo.lugaresCreados.size)
    }
}
