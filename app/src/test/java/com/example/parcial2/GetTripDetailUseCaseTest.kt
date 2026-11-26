package com.example.parcial2

import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.logic.usecase.GetTripDetailUseCase
import com.example.parcial2.model.Place
import com.example.parcial2.model.Trip
import com.example.parcial2.model.TripDetail
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetTripDetailUseCaseTest {

    private class FakeTripRepository(private val detalle: TripDetail) : TripRepository {
        override suspend fun getTrips(): Result<List<Trip>> = Result.failure(Exception())
        override suspend fun createTrip(name: String, destination: String, startDate: String, endDate: String): Result<Trip> = Result.failure(Exception())
        override suspend fun getTripDetail(id: String): Result<TripDetail> {
            return if (id == detalle.id) Result.success(detalle) else Result.failure(Exception("No encontrado"))
        }
    }

    @Test
    fun invoke_retornaDetalleYResumenExitosamente() = runBlocking {
        val lugares = listOf(
            Place("101", "1", "Hotel Coliseo", "Alojamiento", "Roma", 1, "2026-06-11", amount = 120.0),
            Place("102", "1", "Trattoria", "Cena pasta", "Roma", 2, "2026-06-12", amount = 35.0)
        )
        val detalleFake = TripDetail(
            id = "1",
            name = "Europa 2026",
            destination = "Italia",
            startDate = "2026-06-10",
            endDate = "2026-06-20",
            places = lugares,
            placesCount = lugares.size
        )
        val repo = FakeTripRepository(detalleFake)
        val useCase = GetTripDetailUseCase(repo)

        val result = useCase("1")

        assertTrue(result.isSuccess)
        val detail = result.getOrNull()
        assertEquals("Europa 2026", detail?.name)
        assertEquals(2, detail?.places?.size)
        val totalExpenses = detail?.places?.sumOf { it.amount } ?: 0.0
        assertEquals(155.0, totalExpenses, 0.001)
    }
}
