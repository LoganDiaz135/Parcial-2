package com.example.parcial2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial2.ui.PantallaDetalleViaje
import com.example.parcial2.ui.PantallaFormularioLugar
import com.example.parcial2.ui.PantallaFormularioViaje
import com.example.parcial2.ui.PantallaInicioViajes
import com.example.parcial2.ui.theme.Parcial2Theme
import com.example.parcial2.viewmodel.PlaceViewModel
import com.example.parcial2.viewmodel.TripDetailViewModel
import com.example.parcial2.viewmodel.TripViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Parcial2Theme {
                AppViajesContent()
            }
        }
    }
}

@Composable
fun AppViajesContent(
    tripViewModel: TripViewModel = viewModel(factory = TripViewModel.Factory()),
    detailViewModel: TripDetailViewModel = viewModel(factory = TripDetailViewModel.Factory()),
    placeViewModel: PlaceViewModel = viewModel(factory = PlaceViewModel.Factory())
) {
    val contexto = LocalContext.current
    var pantallaActual by remember { mutableStateOf("inicio") }
    var tripIdSeleccionado by remember { mutableStateOf("") }

    val uiState by tripViewModel.uiState.collectAsState()
    val formState by tripViewModel.formState.collectAsState()
    val detailUiState by detailViewModel.uiState.collectAsState()
    val placeFormState by placeViewModel.formState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (pantallaActual) {
            "inicio" -> {
                PantallaInicioViajes(
                    uiState = uiState,
                    alCrearViaje = {
                        tripViewModel.limpiarFormulario()
                        pantallaActual = "crear"
                    },
                    alSeleccionarViaje = { viaje ->
                        tripIdSeleccionado = viaje.id
                        detailViewModel.cargarDetalle(viaje.id)
                        pantallaActual = "detalle"
                    },
                    alReintentar = {
                        tripViewModel.cargarViajes()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            "crear" -> {
                PantallaFormularioViaje(
                    nombre = formState.nombre,
                    destino = formState.destino,
                    fechaInicio = formState.fechaInicio,
                    fechaFin = formState.fechaFin,
                    mensajeError = formState.mensajeError,
                    alCambiarNombre = { tripViewModel.onNombreChange(it) },
                    alCambiarDestino = { tripViewModel.onDestinoChange(it) },
                    alCambiarFechaInicio = { tripViewModel.onFechaInicioChange(it) },
                    alCambiarFechaFin = { tripViewModel.onFechaFinChange(it) },
                    alGuardar = {
                        tripViewModel.guardarViaje {
                            Toast.makeText(contexto, "¡Viaje creado exitosamente!", Toast.LENGTH_SHORT).show()
                            pantallaActual = "inicio"
                        }
                    },
                    alVolver = {
                        pantallaActual = "inicio"
                    }
                )
            }

            "detalle" -> {
                PantallaDetalleViaje(
                    uiState = detailUiState,
                    alVolver = {
                        pantallaActual = "inicio"
                    },
                    alAgregarLugar = { tripId, startDate, endDate ->
                        placeViewModel.inicializarFormulario(tripId, startDate, endDate)
                        pantallaActual = "lugar"
                    },
                    alReintentar = {
                        if (tripIdSeleccionado.isNotBlank()) {
                            detailViewModel.cargarDetalle(tripIdSeleccionado)
                        }
                    }
                )
            }

            "lugar" -> {
                PantallaFormularioLugar(
                    nombre = placeFormState.nombre,
                    descripcion = placeFormState.descripcion,
                    ciudad = placeFormState.ciudad,
                    categoriaSeleccionadaId = placeFormState.categoriaSeleccionadaId,
                    categorias = placeFormState.categorias,
                    fechaTentativa = placeFormState.fechaTentativa,
                    fechaInicioViaje = placeFormState.tripStartDate,
                    fechaFinViaje = placeFormState.tripEndDate,
                    mensajeError = placeFormState.mensajeError,
                    alCambiarNombre = { placeViewModel.onNombreChange(it) },
                    alCambiarDescripcion = { placeViewModel.onDescripcionChange(it) },
                    alCambiarCiudad = { placeViewModel.onCiudadChange(it) },
                    alCambiarCategoria = { placeViewModel.onCategoriaChange(it) },
                    alCambiarFechaTentativa = { placeViewModel.onFechaTentativaChange(it) },
                    alGuardar = {
                        placeViewModel.guardarLugar {
                            Toast.makeText(contexto, "¡Lugar registrado exitosamente!", Toast.LENGTH_SHORT).show()
                            detailViewModel.cargarDetalle(placeFormState.tripId)
                            pantallaActual = "detalle"
                        }
                    },
                    alVolver = {
                        pantallaActual = "detalle"
                    }
                )
            }
        }
    }
}
