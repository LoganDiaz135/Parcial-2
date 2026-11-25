package com.example.parcial2.logic.repository

import com.example.parcial2.model.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}
