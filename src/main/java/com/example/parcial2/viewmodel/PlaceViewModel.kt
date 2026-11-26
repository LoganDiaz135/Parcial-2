package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parcial2.data.api.RetrofitClient
import com.example.parcial2.data.remote.ViajesRemoteDataSource
import com.example.parcial2.data.repository.CategoryRepositoryImpl
import com.example.parcial2.data.repository.PlaceRepositoryImpl
import com.example.parcial2.logic.repository.CategoryRepository
import com.example.parcial2.logic.repository.PlaceRepository
import com.example.parcial2.logic.usecase.CreatePlaceUseCase
import com.example.parcial2.logic.usecase.GetCategoriesUseCase
import com.example.parcial2.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlaceFormState(
    val tripId: String = "",
    val tripStartDate: String = "",
    val tripEndDate: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val ciudad: String = "",
    val categoriaSeleccionadaId: Int = 0,
    val fechaTentativa: String = "",
    val categorias: List<Category> = emptyList(),
    val cargandoCategorias: Boolean = false,
    val mensajeError: String? = null,
    val esGuardando: Boolean = false
)

class PlaceViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createPlaceUseCase: CreatePlaceUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(PlaceFormState())
    val formState: StateFlow<PlaceFormState> = _formState.asStateFlow()

    fun inicializarFormulario(tripId: String, tripStartDate: String, tripEndDate: String) {
        _formState.update {
            PlaceFormState(
                tripId = tripId,
                tripStartDate = tripStartDate,
                tripEndDate = tripEndDate
            )
        }
        cargarCategorias()
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            _formState.update { it.copy(cargandoCategorias = true) }
            getCategoriesUseCase()
                .onSuccess { lista ->
                    _formState.update {
                        it.copy(
                            categorias = lista,
                            cargandoCategorias = false,
                            categoriaSeleccionadaId = if (lista.isNotEmpty()) lista.first().id else 0
                        )
                    }
                }
                .onFailure { error ->
                    _formState.update {
                        it.copy(
                            cargandoCategorias = false,
                            mensajeError = "No se pudieron cargar las categorías: ${error.message}"
                        )
                    }
                }
        }
    }

    fun onNombreChange(v: String) = _formState.update { it.copy(nombre = v, mensajeError = null) }
    fun onDescripcionChange(v: String) = _formState.update { it.copy(descripcion = v, mensajeError = null) }
    fun onCiudadChange(v: String) = _formState.update { it.copy(ciudad = v, mensajeError = null) }
    fun onCategoriaChange(id: Int) = _formState.update { it.copy(categoriaSeleccionadaId = id, mensajeError = null) }
    fun onFechaTentativaChange(v: String) = _formState.update { it.copy(fechaTentativa = v, mensajeError = null) }

    fun guardarLugar(onSuccess: () -> Unit) {
        val s = _formState.value
        _formState.update { it.copy(esGuardando = true, mensajeError = null) }

        viewModelScope.launch {
            createPlaceUseCase(
                tripId = s.tripId,
                name = s.nombre,
                description = s.descripcion,
                city = s.ciudad,
                categoryId = s.categoriaSeleccionadaId,
                tentativeDate = s.fechaTentativa,
                tripStartDate = s.tripStartDate,
                tripEndDate = s.tripEndDate
            ).onSuccess {
                _formState.update { it.copy(esGuardando = false) }
                onSuccess()
            }.onFailure { error ->
                _formState.update {
                    it.copy(
                        esGuardando = false,
                        mensajeError = error.message ?: "Error al registrar el lugar."
                    )
                }
            }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val remoteDataSource = ViajesRemoteDataSource(RetrofitClient.apiService)
                val categoryRepo: CategoryRepository = CategoryRepositoryImpl(remoteDataSource)
                val placeRepo: PlaceRepository = PlaceRepositoryImpl(remoteDataSource)
                val getCategoriesUseCase = GetCategoriesUseCase(categoryRepo)
                val createPlaceUseCase = CreatePlaceUseCase(placeRepo)
                return PlaceViewModel(getCategoriesUseCase, createPlaceUseCase) as T
            }
        }
    }
}
