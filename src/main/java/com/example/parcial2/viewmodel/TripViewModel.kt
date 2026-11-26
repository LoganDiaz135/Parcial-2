package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parcial2.data.api.RetrofitClient
import com.example.parcial2.data.remote.ViajesRemoteDataSource
import com.example.parcial2.data.repository.TripRepositoryImpl
import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.logic.usecase.CreateTripUseCase
import com.example.parcial2.logic.usecase.GetTripsUseCase
import com.example.parcial2.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface TripListUiState {
    object Loading : TripListUiState
    data class Success(val trips: List<Trip>) : TripListUiState
    data class Error(val message: String) : TripListUiState
}

data class TripFormState(
    val nombre: String = "",
    val destino: String = "",
    val fechaInicio: String = "",
    val fechaFin: String = "",
    val mensajeError: String? = null,
    val esGuardando: Boolean = false
)

class TripViewModel(
    private val getTripsUseCase: GetTripsUseCase,
    private val createTripUseCase: CreateTripUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TripListUiState>(TripListUiState.Loading)
    val uiState: StateFlow<TripListUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(TripFormState())
    val formState: StateFlow<TripFormState> = _formState.asStateFlow()

    init {
        cargarViajes()
    }

    fun cargarViajes() {
        viewModelScope.launch {
            _uiState.value = TripListUiState.Loading
            getTripsUseCase()
                .onSuccess { lista ->
                    _uiState.value = TripListUiState.Success(lista)
                }
                .onFailure { error ->
                    _uiState.value = TripListUiState.Error(
                        error.message ?: "Error de conexión con la API."
                    )
                }
        }
    }

    fun onNombreChange(nombre: String) {
        _formState.update { it.copy(nombre = nombre, mensajeError = null) }
    }

    fun onDestinoChange(destino: String) {
        _formState.update { it.copy(destino = destino, mensajeError = null) }
    }

    fun onFechaInicioChange(fecha: String) {
        _formState.update { it.copy(fechaInicio = fecha, mensajeError = null) }
    }

    fun onFechaFinChange(fecha: String) {
        _formState.update { it.copy(fechaFin = fecha, mensajeError = null) }
    }

    fun limpiarFormulario() {
        _formState.value = TripFormState()
    }

    fun guardarViaje(onSuccess: () -> Unit) {
        val actual = _formState.value
        _formState.update { it.copy(esGuardando = true, mensajeError = null) }

        viewModelScope.launch {
            createTripUseCase(
                name = actual.nombre,
                destination = actual.destino,
                startDate = actual.fechaInicio,
                endDate = actual.fechaFin
            ).onSuccess {
                _formState.update { it.copy(esGuardando = false) }
                limpiarFormulario()
                cargarViajes()
                onSuccess()
            }.onFailure { error ->
                _formState.update {
                    it.copy(
                        esGuardando = false,
                        mensajeError = error.message ?: "Error al guardar el viaje."
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
                val repository: TripRepository = TripRepositoryImpl(remoteDataSource)
                val getTripsUseCase = GetTripsUseCase(repository)
                val createTripUseCase = CreateTripUseCase(repository)
                return TripViewModel(getTripsUseCase, createTripUseCase) as T
            }
        }
    }
}
