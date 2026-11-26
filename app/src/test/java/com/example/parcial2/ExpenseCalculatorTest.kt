package com.example.parcial2

import com.example.parcial2.logic.calculator.ExpenseCalculator
import com.example.parcial2.model.Place
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExpenseCalculatorTest {

    private lateinit var calculator: ExpenseCalculator

    @Before
    fun setUp() {
        calculator = ExpenseCalculator()
    }

    @Test
    fun calculateTotal_sumaTodosLosGastosCorrectamente() {
        val listaGastos = listOf(
            Place("1", "trip-1", "Hotel", "Hospedaje", "Roma", 1, "2026-06-11", amount = 150.0),
            Place("2", "trip-1", "Museo", "Entrada", "Roma", 2, "2026-06-12", amount = 25.0),
            Place("3", "trip-1", "Restaurante", "Cena", "Roma", 3, "2026-06-13", amount = 45.5)
        )

        val total = calculator.calculateTotal(listaGastos)

        assertEquals(220.5, total, 0.001)
    }

    @Test
    fun calculateTotalForTrip_filtraSoloGastosDelViajeEspecificado() {
        val listaGastosMezclados = listOf(
            Place("1", "trip-1", "Hotel Roma", "Hospedaje", "Roma", 1, "2026-06-11", amount = 200.0),
            Place("2", "trip-1", "Cena Roma", "Comida", "Roma", 2, "2026-06-12", amount = 50.0),
            Place("3", "trip-2", "Hotel Paris", "Hospedaje", "Paris", 1, "2026-07-01", amount = 300.0)
        )

        val totalTrip1 = calculator.calculateTotalForTrip(listaGastosMezclados, "trip-1")
        val totalTrip2 = calculator.calculateTotalForTrip(listaGastosMezclados, "trip-2")

        assertEquals(250.0, totalTrip1, 0.001)
        assertEquals(300.0, totalTrip2, 0.001)
    }

    @Test
    fun calculateTotal_conListaVacia_retornaCero() {
        val total = calculator.calculateTotal(emptyList())
        assertEquals(0.0, total, 0.001)
    }
}
