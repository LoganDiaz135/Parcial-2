package com.example.parcial2

import com.example.parcial2.logic.repository.CategoryRepository
import com.example.parcial2.logic.repository.PlaceRepository
import com.example.parcial2.logic.usecase.CreatePlaceUseCase
import com.example.parcial2.logic.usecase.GetCategoriesUseCase
import com.example.parcial2.model.Category
import com.example.parcial2.model.Place
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpenseViewModelTest {

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
            val nuevo = Place(
                id = "1",
                tripId = tripId,
                name = name,
                description = description,
                city = city,
                categoryId = categoryId,
                tentativeDate = tentativeDate,
                amount = amount
            )
            listaGastos.add(nuevo)
            return Result.success(nuevo)
        }
    }

    private class FakeCategoryRepository : CategoryRepository {
        override suspend fun getCategories(): Result<List<Category>> {
            return Result.success(listOf(Category(1, "Hospedaje", "Hoteles")))
        }
    }

    @Test
    fun testCrearGastoYObtenerCategoriasSinComponentesVisuales() = runBlocking {
        val placeRepo = FakePlaceRepository()
        val categoryRepo = FakeCategoryRepository()

        val createPlaceUseCase = CreatePlaceUseCase(placeRepo)
        val getCategoriesUseCase = GetCategoriesUseCase(categoryRepo)

        val categoriesResult = getCategoriesUseCase()
        assertTrue(categoriesResult.isSuccess)
        assertEquals(1, categoriesResult.getOrNull()?.size)

        val createResult = createPlaceUseCase(
            tripId = "1",
            name = "Hotel Roma",
            description = "3 noches",
            city = "Roma",
            categoryId = 1,
            tentativeDate = "2026-06-11",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 300.0
        )
        assertTrue(createResult.isSuccess)
        assertEquals("Hotel Roma", createResult.getOrNull()?.name)
    }
}
