package com.example.parcial2.model

data class Place(
    val id: String,
    val tripId: String,
    val name: String,
    val description: String,
    val city: String,
    val categoryId: Int,
    val tentativeDate: String,
    val category: Category? = null,
    val amount: Double = 0.0
) {
    val value: Double get() = amount
    val expenseDate: String get() = tentativeDate
}

typealias Expense = Place
