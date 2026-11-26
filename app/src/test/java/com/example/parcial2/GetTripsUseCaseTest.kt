package com.example.parcial2

import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.logic.usecase.GetTripsUseCase
import com.example.parcial2.model.Trip
import com.example.parcial2.model.TripDetail
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetTripsUseCaseTest {

    private class FakeTripRepository(private val listaViajes: List<Trip>) : TripRepository {
        override suspend fun getTrips(): Result<List<Trip>> = Result.success(listaViajes)
        override suspend fun createTrip(name: String, destination: String, startDate: String, endDate: String): Result<Trip> = Result.failure(Exception())
        override suspend fun getTripDetail(id: String): Result<TripDetail> = Result.failure(Exception())
    }

    @Test
    fun invoke_retornaListaDeViajesExitosamente() = runBlocking {
        val datosFalsos = listOf(
            Trip("1", "Europa 2026", "Italia", "2026-06-10", "2026-06-20"),
            Trip("2", "Convención", "Cancún", "2026-09-15", "2026-09-22")
        )
        val fakeRepo = FakeTripRepository(datosFalsos)
        val useCase = GetTripsUseCase(fakeRepo)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Europa 2026", result.getOrNull()?.first()?.name)
    }
}
