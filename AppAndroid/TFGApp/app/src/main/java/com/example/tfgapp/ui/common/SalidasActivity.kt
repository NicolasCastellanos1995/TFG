// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/common/SalidasActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tfgapp.R
import com.example.tfgapp.data.model.ProductExitRequest
import com.example.tfgapp.data.network.RetrofitClient
import kotlinx.coroutines.launch

// Clase principal de esta pantalla o componente de la aplicación.
class SalidasActivity : AppCompatActivity() {

    private lateinit var retProducto: EditText
    private lateinit var retCantidad: EditText
    private lateinit var btnSalir: Button

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salidas)

        retProducto = findViewById(R.id.retProducto)
        retCantidad = findViewById(R.id.retCantidad)
        btnSalir = findViewById(R.id.btnTrasladar)

        btnSalir.setOnClickListener {
            registrarSalida()
        }
    }

    // Función auxiliar que gestiona la lógica de `registrarSalida`.
    private fun registrarSalida() {
        val productText = retProducto.text.toString().trim()
        val quantityText = retCantidad.text.toString().trim()

        if (productText.isEmpty() || quantityText.isEmpty()) {
            Toast.makeText(this, "Completa producto y cantidad", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityText.toIntOrNull()

        if (quantity == null || quantity <= 0) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        val request = ProductExitRequest(
            product = productText,
            quantity = quantity
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createProductExit(
                    "Bearer $token",
                    request
                )

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@SalidasActivity,
                        response.body()?.get("message") ?: "Salida registrada correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    limpiarCampos()
                } else {
                    val error = response.errorBody()?.string()

                    Log.e("SALIDA_ERROR", "Código: ${response.code()} | Error: $error")

                    Toast.makeText(
                        this@SalidasActivity,
                        "Error ${response.code()}: $error",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("SALIDA_ERROR", "Error de conexión", e)

                Toast.makeText(
                    this@SalidasActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Función auxiliar que gestiona la lógica de `limpiarCampos`.
    private fun limpiarCampos() {
        retProducto.text.clear()
        retCantidad.text.clear()
        retProducto.requestFocus()
    }
}
