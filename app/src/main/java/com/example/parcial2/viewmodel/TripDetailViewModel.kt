package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parcial2.data.api.RetrofitClient
import com.example.parcial2.data.remote.ViajesRemoteDataSource
import com.example.parcial2.data.repository.TripRepositoryImpl
import com.example.parcial2.logic.repository.TripRepository
import com.example.parcial2.logic.usecase.GetTripDetailUseCase
import com.example.parcial2.model.TripDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TripDetailUiState {
    object Loading : TripDetailUiState
    data class Success(val tripDetail: TripDetail) : TripDetailUiState
    data class Error(val message: String) : TripDetailUiState
}

class TripDetailViewModel(
    private val getTripDetailUseCase: GetTripDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TripDetailUiState>(TripDetailUiState.Loading)
    val uiState: StateFlow<TripDetailUiState> = _uiState.asStateFlow()

    fun cargarDetalle(tripId: String) {
        viewModelScope.launch {
            _uiState.value = TripDetailUiState.Loading
            getTripDetailUseCase(tripId)
                .onSuccess { detalle ->
                    _uiState.value = TripDetailUiState.Success(detalle)
                }
                .onFailure { error ->
                    _uiState.value = TripDetailUiState.Error(
                        error.message ?: "No se pudo cargar el detalle del viaje."
                    )
                }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val remoteDataSource = ViajesRemoteDataSource(RetrofitClient.apiService)
                val repository: TripRepository = TripRepositoryImpl(remoteDataSource)
                val getTripDetailUseCase = GetTripDetailUseCase(repository)
                return TripDetailViewModel(getTripDetailUseCase) as T
            }
        }
    }
}
