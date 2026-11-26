package com.example.parcial2

import com.example.parcial2.logic.repository.CategoryRepository
import com.example.parcial2.logic.usecase.GetCategoriesUseCase
import com.example.parcial2.model.Category
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCategoriesUseCaseTest {

    private class FakeCategoryRepository(private val categorias: List<Category>) : CategoryRepository {
        override suspend fun getCategories(): Result<List<Category>> {
            return Result.success(categorias)
        }
    }

    @Test
    fun invoke_retornaListaDeCategoriasExitosamente() = runBlocking {
        val listaFake = listOf(
            Category(1, "Hospedaje", "Hoteles y alojamientos"),
            Category(2, "Alimentación", "Restaurantes y supermercados"),
            Category(3, "Transporte", "Vuelos, trenes y buses")
        )
        val repo = FakeCategoryRepository(listaFake)
        val useCase = GetCategoriesUseCase(repo)

        val result = useCase()

        assertTrue(result.isSuccess)
        val categories = result.getOrNull()
        assertEquals(3, categories?.size)
        assertEquals("Hospedaje", categories?.first()?.name)
        assertEquals(1, categories?.first()?.id)
    }
}
