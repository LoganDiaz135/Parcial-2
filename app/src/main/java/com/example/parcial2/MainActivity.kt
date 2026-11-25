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
import com.example.parcial2.ui.PantallaFormularioViaje
import com.example.parcial2.ui.PantallaInicioViajes
import com.example.parcial2.ui.theme.Parcial2Theme
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
    viewModel: TripViewModel = viewModel(factory = TripViewModel.Factory())
) {
    val contexto = LocalContext.current
    var pantallaActual by remember { mutableStateOf("inicio") }

    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (pantallaActual) {
            "inicio" -> {
                PantallaInicioViajes(
                    uiState = uiState,
                    alCrearViaje = {
                        viewModel.limpiarFormulario()
                        pantallaActual = "crear"
                    },
                    alSeleccionarViaje = { viaje ->
                        Toast.makeText(
                            contexto,
                            "Viaje seleccionado: ${viaje.name} (${viaje.destination})",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    alReintentar = {
                        viewModel.cargarViajes()
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
                    alCambiarNombre = { viewModel.onNombreChange(it) },
                    alCambiarDestino = { viewModel.onDestinoChange(it) },
                    alCambiarFechaInicio = { viewModel.onFechaInicioChange(it) },
                    alCambiarFechaFin = { viewModel.onFechaFinChange(it) },
                    alGuardar = {
                        viewModel.guardarViaje {
                            Toast.makeText(contexto, "¡Viaje creado en la API exitosamente!", Toast.LENGTH_SHORT).show()
                            pantallaActual = "inicio"
                        }
                    },
                    alVolver = {
                        pantallaActual = "inicio"
                    }
                )
            }
        }
    }
}
