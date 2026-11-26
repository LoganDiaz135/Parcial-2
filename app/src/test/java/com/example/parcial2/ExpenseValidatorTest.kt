package com.example.parcial2

import com.example.parcial2.logic.validation.TripValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExpenseValidatorTest {

    private lateinit var validator: TripValidator

    @Before
    fun setUp() {
        validator = TripValidator()
    }

    @Test
    fun validateExpense_conCamposValidosYAmountPositivo_retornaValido() {
        val result = validator.validateExpense(
            name = "Almuerzo en Roma",
            description = "Pasta y café",
            city = "Roma",
            categoryId = 1,
            expenseDate = "2026-06-12",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 35.5
        )
        assertTrue(result.isValid)
    }

    @Test
    fun validateExpense_conAmountCeroONegativo_retornaInvalido() {
        val resultCero = validator.validateExpense(
            name = "Cena",
            description = "Pizza",
            city = "Roma",
            categoryId = 1,
            expenseDate = "2026-06-12",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 0.0
        )
        assertFalse(resultCero.isValid)
        assertEquals("El valor del gasto debe ser mayor a cero.", resultCero.errorMessage)

        val resultNegativo = validator.validateExpense(
            name = "Cena",
            description = "Pizza",
            city = "Roma",
            categoryId = 1,
            expenseDate = "2026-06-12",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = -10.0
        )
        assertFalse(resultNegativo.isValid)
        assertEquals("El valor del gasto debe ser mayor a cero.", resultNegativo.errorMessage)
    }

    @Test
    fun validateExpense_conFechaFueraDeRango_retornaInvalido() {
        val resultAnterior = validator.validateExpense(
            name = "Tiquete bus",
            description = "Traslado",
            city = "Roma",
            categoryId = 2,
            expenseDate = "2026-06-01",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 20.0
        )
        assertFalse(resultAnterior.isValid)
        assertTrue(resultAnterior.errorMessage?.contains("anterior") == true)

        val resultPosterior = validator.validateExpense(
            name = "Souvenirs",
            description = "Recuerdos",
            city = "Roma",
            categoryId = 3,
            expenseDate = "2026-06-25",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20",
            amount = 45.0
        )
        assertFalse(resultPosterior.isValid)
        assertTrue(resultPosterior.errorMessage?.contains("posterior") == true)
    }
}
