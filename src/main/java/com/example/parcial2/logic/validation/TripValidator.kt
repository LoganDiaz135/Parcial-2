package com.example.parcial2.logic.validation

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripValidator {

    fun validateTrip(
        name: String,
        destination: String,
        startDate: String,
        endDate: String
    ): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "El nombre del viaje es obligatorio.")
        }
        if (destination.isBlank()) {
            return ValidationResult(false, "El destino del viaje es obligatorio.")
        }
        if (startDate.isBlank()) {
            return ValidationResult(false, "La fecha de inicio es obligatoria.")
        }
        if (endDate.isBlank()) {
            return ValidationResult(false, "La fecha de finalización es obligatoria.")
        }

        val start = parseDate(startDate)
            ?: return ValidationResult(false, "La fecha de inicio no es válida (usar AAAA-MM-DD).")
        val end = parseDate(endDate)
            ?: return ValidationResult(false, "La fecha de finalización no es válida (usar AAAA-MM-DD).")

        if (end.before(start)) {
            return ValidationResult(false, "La fecha de finalización no puede ser anterior a la fecha de inicio.")
        }

        return ValidationResult(true)
    }

    fun validatePlace(
        name: String,
        description: String,
        city: String,
        categoryId: Int,
        tentativeDate: String,
        tripStartDate: String,
        tripEndDate: String
    ): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(false, "El nombre del lugar es obligatorio.")
        }
        if (description.isBlank()) {
            return ValidationResult(false, "La descripción del lugar es obligatoria.")
        }
        if (city.isBlank()) {
            return ValidationResult(false, "La ciudad es obligatoria.")
        }
        if (categoryId <= 0) {
            return ValidationResult(false, "Debe seleccionar una categoría válida.")
        }
        if (tentativeDate.isBlank()) {
            return ValidationResult(false, "La fecha tentativa de visita es obligatoria.")
        }

        val visitDate = parseDate(tentativeDate)
            ?: return ValidationResult(false, "La fecha tentativa no es válida (usar AAAA-MM-DD).")
        val start = parseDate(tripStartDate)
        val end = parseDate(tripEndDate)

        if (start != null && visitDate.before(start)) {
            return ValidationResult(false, "La fecha del lugar no puede ser anterior al inicio del viaje ($tripStartDate).")
        }
        if (end != null && visitDate.after(end)) {
            return ValidationResult(false, "La fecha del lugar no puede ser posterior a la finalización del viaje ($tripEndDate).")
        }

        return ValidationResult(true)
    }

    private fun parseDate(dateStr: String): Date? {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                isLenient = false
            }
            format.parse(dateStr.trim())
        } catch (e: ParseException) {
            null
        }
    }
}
