package com.example.parcial2

import com.example.parcial2.logic.validation.TripValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TripValidatorTest {

    private lateinit var validator: TripValidator

    @Before
    fun setUp() {
        validator = TripValidator()
    }

    @Test
    fun validateTrip_conDatosValidos_retornaValido() {
        val result = validator.validateTrip(
            name = "Europa 2026",
            destination = "Italia",
            startDate = "2026-06-10",
            endDate = "2026-06-20"
        )
        assertTrue(result.isValid)
    }

    @Test
    fun validateTrip_conNombreVacio_retornaInvalido() {
        val result = validator.validateTrip(
            name = "   ",
            destination = "Italia",
            startDate = "2026-06-10",
            endDate = "2026-06-20"
        )
        assertFalse(result.isValid)
        assertEquals("El nombre del viaje es obligatorio.", result.errorMessage)
    }

    @Test
    fun validateTrip_conDestinoVacio_retornaInvalido() {
        val result = validator.validateTrip(
            name = "Europa 2026",
            destination = "",
            startDate = "2026-06-10",
            endDate = "2026-06-20"
        )
        assertFalse(result.isValid)
        assertEquals("El destino del viaje es obligatorio.", result.errorMessage)
    }

    @Test
    fun validateTrip_conFechaFinAnteriorAInicio_retornaInvalido() {
        val result = validator.validateTrip(
            name = "Europa 2026",
            destination = "Italia",
            startDate = "2026-06-20",
            endDate = "2026-06-10"
        )
        assertFalse(result.isValid)
        assertEquals("La fecha de finalización no puede ser anterior a la fecha de inicio.", result.errorMessage)
    }

    @Test
    fun validatePlace_conDatosValidosYFechaEnRango_retornaValido() {
        val result = validator.validatePlace(
            name = "Coliseo Romano",
            description = "Anfiteatro histórico",
            city = "Roma",
            categoryId = 1,
            tentativeDate = "2026-06-15",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20"
        )
        assertTrue(result.isValid)
    }

    @Test
    fun validatePlace_conFechaAnteriorAlViaje_retornaInvalido() {
        val result = validator.validatePlace(
            name = "Coliseo Romano",
            description = "Anfiteatro histórico",
            city = "Roma",
            categoryId = 1,
            tentativeDate = "2026-06-05",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20"
        )
        assertFalse(result.isValid)
        assertTrue(result.errorMessage?.contains("anterior") == true)
    }

    @Test
    fun validatePlace_conFechaPosteriorAlViaje_retornaInvalido() {
        val result = validator.validatePlace(
            name = "Coliseo Romano",
            description = "Anfiteatro histórico",
            city = "Roma",
            categoryId = 1,
            tentativeDate = "2026-06-25",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20"
        )
        assertFalse(result.isValid)
        assertTrue(result.errorMessage?.contains("posterior") == true)
    }

    @Test
    fun validatePlace_sinCategoriaSeleccionada_retornaInvalido() {
        val result = validator.validatePlace(
            name = "Coliseo Romano",
            description = "Anfiteatro histórico",
            city = "Roma",
            categoryId = 0,
            tentativeDate = "2026-06-15",
            tripStartDate = "2026-06-10",
            tripEndDate = "2026-06-20"
        )
        assertFalse(result.isValid)
        assertEquals("Debe seleccionar una categoría válida.", result.errorMessage)
    }
}
