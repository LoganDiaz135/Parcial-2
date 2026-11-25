@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.parcial2.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun PantallaFormularioViaje(
    nombre: String,
    destino: String,
    fechaInicio: String,
    fechaFin: String,
    mensajeError: String?,
    alCambiarNombre: (String) -> Unit,
    alCambiarDestino: (String) -> Unit,
    alCambiarFechaInicio: (String) -> Unit,
    alCambiarFechaFin: (String) -> Unit,
    alGuardar: () -> Unit,
    alVolver: () -> Unit
) {
    val contexto = LocalContext.current
    val calendario = Calendar.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear Nuevo Viaje",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = alCambiarNombre,
                label = { Text("Nombre del viaje") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = destino,
                onValueChange = alCambiarDestino,
                label = { Text("Destino") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = {
                    DatePickerDialog(
                        contexto,
                        { _, anio, mes, dia ->
                            val mesFormateado = String.format("%02d", mes + 1)
                            val diaFormateado = String.format("%02d", dia)
                            alCambiarFechaInicio("$anio-$mesFormateado-$diaFormateado")
                        },
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (fechaInicio.isNotBlank()) "  Fecha de Inicio: $fechaInicio" else "  Seleccionar Fecha de Inicio",
                    fontWeight = FontWeight.Medium
                )
            }

            OutlinedButton(
                onClick = {
                    DatePickerDialog(
                        contexto,
                        { _, anio, mes, dia ->
                            val mesFormateado = String.format("%02d", mes + 1)
                            val diaFormateado = String.format("%02d", dia)
                            alCambiarFechaFin("$anio-$mesFormateado-$diaFormateado")
                        },
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (fechaFin.isNotBlank()) "  Fecha de Finalización: $fechaFin" else "  Seleccionar Fecha de Finalización",
                    fontWeight = FontWeight.Medium
                )
            }

            if (!mensajeError.isNullOrBlank()) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Button(
                onClick = alGuardar,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Guardar Viaje",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
