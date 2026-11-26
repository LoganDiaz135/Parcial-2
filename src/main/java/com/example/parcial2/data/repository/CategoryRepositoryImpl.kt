package com.example.parcial2.data.repository

import com.example.parcial2.data.mapper.toDomain
import com.example.parcial2.data.remote.ViajesRemoteDataSource
import com.example.parcial2.logic.repository.CategoryRepository
import com.example.parcial2.model.Category

class CategoryRepositoryImpl(
    private val remoteDataSource: ViajesRemoteDataSource
) : CategoryRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val dtoList = remoteDataSource.getCategories()
            Result.success(dtoList.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
