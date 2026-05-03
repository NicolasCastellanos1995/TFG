// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/common/TransferenciasActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.common

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfgapp.R
import com.example.tfgapp.data.model.LocationResponse
import com.example.tfgapp.data.model.TransferRequest
import com.example.tfgapp.data.network.RetrofitClient
import kotlinx.coroutines.launch

// Clase principal de esta pantalla o componente de la aplicación.
class TransferenciasActivity : AppCompatActivity() {

    private lateinit var etOrigen: AutoCompleteTextView
    private lateinit var etDestino: AutoCompleteTextView
    private lateinit var btnTrasladar: Button

    private var originLocations: List<LocationResponse> = emptyList()
    private var destinationLocations: List<LocationResponse> = emptyList()

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferencias)

        etOrigen = findViewById(R.id.etOrigen)
        etDestino = findViewById(R.id.etDestino)
        btnTrasladar = findViewById(R.id.btnTrasladar)

        cargarUbicaciones()

        btnTrasladar.setOnClickListener {
            registrarTransferencia()
        }
    }

    // Función auxiliar que gestiona la lógica de `cargarUbicaciones`.
    private fun cargarUbicaciones() {
        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val originResponse =
                    RetrofitClient.api.getOccupiedLocationsSuspend("Bearer $token")

                val destinationResponse =
                    RetrofitClient.api.getEmptyLocationsSuspend("Bearer $token")

                if (originResponse.isSuccessful && destinationResponse.isSuccessful) {
                    originLocations = originResponse.body() ?: emptyList()
                    destinationLocations = destinationResponse.body() ?: emptyList()

                    val originItems = originLocations.map { "${it.code} - ${it.name}" }
                    val destinationItems = destinationLocations.map { "${it.code} - ${it.name}" }

                    val originAdapter = ArrayAdapter(
                        this@TransferenciasActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        originItems
                    )

                    val destinationAdapter = ArrayAdapter(
                        this@TransferenciasActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        destinationItems
                    )

                    etOrigen.setAdapter(originAdapter)
                    etDestino.setAdapter(destinationAdapter)

                    etOrigen.setOnClickListener { etOrigen.showDropDown() }
                    etDestino.setOnClickListener { etDestino.showDropDown() }

                    if (originLocations.isEmpty()) {
                        Toast.makeText(
                            this@TransferenciasActivity,
                            "No hay ubicaciones con stock para transferir",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    if (destinationLocations.isEmpty()) {
                        Toast.makeText(
                            this@TransferenciasActivity,
                            "No hay ubicaciones vacías disponibles",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    val originError = originResponse.errorBody()?.string()
                    val destinationError = destinationResponse.errorBody()?.string()

                    Log.e(
                        "TRANSFER_ERROR",
                        "Origen ${originResponse.code()}: $originError | Destino ${destinationResponse.code()}: $destinationError"
                    )

                    Toast.makeText(
                        this@TransferenciasActivity,
                        "Error cargando ubicaciones",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("TRANSFER_ERROR", "Error cargando ubicaciones", e)
                Toast.makeText(
                    this@TransferenciasActivity,
                    "Error de conexión",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Función auxiliar que gestiona la lógica de `registrarTransferencia`.
    private fun registrarTransferencia() {
        val origenText = etOrigen.text.toString().trim()
        val destinoText = etDestino.text.toString().trim()

        if (origenText.isEmpty() || destinoText.isEmpty()) {
            Toast.makeText(this, "Selecciona origen y destino", Toast.LENGTH_SHORT).show()
            return
        }

        val originCode = origenText.substringBefore(" - ").trim()
        val destinationCode = destinoText.substringBefore(" - ").trim()

        val originExists = originLocations.any { it.code.equals(originCode, ignoreCase = true) }
        val destinationExists = destinationLocations.any { it.code.equals(destinationCode, ignoreCase = true) }

        if (!originExists) {
            Toast.makeText(this, "Selecciona una ubicación origen válida", Toast.LENGTH_SHORT).show()
            return
        }

        if (!destinationExists) {
            Toast.makeText(this, "Selecciona una ubicación destino válida", Toast.LENGTH_SHORT).show()
            return
        }

        if (originCode.equals(destinationCode, ignoreCase = true)) {
            Toast.makeText(this, "Origen y destino no pueden ser iguales", Toast.LENGTH_SHORT).show()
            return
        }

        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        val request = TransferRequest(
            originCode = originCode,
            destinationCode = destinationCode
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createTransfer(
                    "Bearer $token",
                    request
                )

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@TransferenciasActivity,
                        response.body()?.get("message")
                            ?: "Transferencia registrada correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    limpiarCampos()
                    cargarUbicaciones()

                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@TransferenciasActivity,
                        "Error ${response.code()}: $error",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("TRANSFER_ERROR", "Error registrando transferencia", e)
                Toast.makeText(
                    this@TransferenciasActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Función auxiliar que gestiona la lógica de `limpiarCampos`.
    private fun limpiarCampos() {
        etOrigen.text.clear()
        etDestino.text.clear()
        etOrigen.requestFocus()
    }
}
