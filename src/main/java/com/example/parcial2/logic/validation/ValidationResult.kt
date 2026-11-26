package com.example.parcial2.logic.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
