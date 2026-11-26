package com.example.parcial2

import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.logic.usecase.CreateTripUseCase
import com.example.parcial2.model.Trip
import com.example.parcial2.model.TripDetail
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateTripUseCaseTest {

    private class FakeTripRepository : TripRepository {
        val tripsCreados = mutableListOf<Trip>()

        override suspend fun getTrips(): Result<List<Trip>> = Result.success(tripsCreados)

        override suspend fun createTrip(
            name: String,
            destination: String,
            startDate: String,
            endDate: String
        ): Result<Trip> {
            val nuevo = Trip(
                id = "1",
                name = name,
                destination = destination,
                startDate = startDate,
                endDate = endDate
            )
            tripsCreados.add(nuevo)
            return Result.success(nuevo)
        }

        override suspend fun getTripDetail(id: String): Result<TripDetail> {
            return Result.failure(Exception("No implementado"))
        }
    }

    @Test
    fun invoke_conDatosValidos_creaViajeExitosamente() = runBlocking {
        val fakeRepo = FakeTripRepository()
        val useCase = CreateTripUseCase(fakeRepo)

        val result = useCase("Europa 2026", "Italia", "2026-06-10", "2026-06-20")

        assertTrue(result.isSuccess)
        assertEquals("Europa 2026", result.getOrNull()?.name)
        assertEquals(1, fakeRepo.tripsCreados.size)
    }

    @Test
    fun invoke_conFechasInvalidas_retornaFallo() = runBlocking {
        val fakeRepo = FakeTripRepository()
        val useCase = CreateTripUseCase(fakeRepo)

        val result = useCase("Europa 2026", "Italia", "2026-06-20", "2026-06-10")

        assertTrue(result.isFailure)
        assertEquals(0, fakeRepo.tripsCreados.size)
    }
}
