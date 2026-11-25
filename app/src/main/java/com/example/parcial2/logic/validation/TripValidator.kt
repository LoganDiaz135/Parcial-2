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
