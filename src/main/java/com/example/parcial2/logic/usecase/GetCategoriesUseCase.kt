package com.example.parcial2.logic.usecase

import com.example.parcial2.logic.repository.CategoryRepository
import com.example.parcial2.model.Category

class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return categoryRepository.getCategories()
    }
}
