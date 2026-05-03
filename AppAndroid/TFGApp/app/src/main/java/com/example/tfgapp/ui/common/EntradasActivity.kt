// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/common/EntradasActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.common

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfgapp.R
import com.example.tfgapp.data.model.ProductEntryRequest
import com.example.tfgapp.data.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Clase principal de esta pantalla o componente de la aplicación.
class EntradasActivity : AppCompatActivity() {

    private lateinit var ingProducto: EditText
    private lateinit var ingCantidad: EditText
    private lateinit var ingLote: EditText
    private lateinit var ingVencimiento: EditText
    private lateinit var btnIngresar: Button

    private var selectedDate: String = ""

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entradas)

        ingProducto = findViewById(R.id.ingProducto)
        ingCantidad = findViewById(R.id.ingCantidad)
        ingLote = findViewById(R.id.ingLote)
        ingVencimiento = findViewById(R.id.ingVencimiento)
        btnIngresar = findViewById(R.id.btnTrasladar)

        ingVencimiento.setOnClickListener { showDatePicker() }
        btnIngresar.setOnClickListener { registrarEntrada() }
    }

    // Función auxiliar que gestiona la lógica de `showDatePicker`.
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val monthFormatted = String.format("%02d", month + 1)
                val dayFormatted = String.format("%02d", day)

                selectedDate = "$year-$monthFormatted-$dayFormatted"
                ingVencimiento.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    // Función auxiliar que gestiona la lógica de `registrarEntrada`.
    private fun registrarEntrada() {
        val productText = ingProducto.text.toString().trim()
        val quantityText = ingCantidad.text.toString().trim()
        val lotCode = ingLote.text.toString().trim()

        if (productText.isEmpty() || quantityText.isEmpty() || lotCode.isEmpty()) {
            Toast.makeText(this, "Completa producto, cantidad y lote", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityText.toIntOrNull()
        if (quantity == null || quantity <= 0) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDate.isNotEmpty() && !fechaEsValida(selectedDate)) {
            Toast.makeText(this, "La fecha debe ser futura", Toast.LENGTH_SHORT).show()
            return
        }

        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        val request = ProductEntryRequest(
            product = productText,
            quantity = quantity,
            lotCode = lotCode,
            expirationDate = if (selectedDate.isEmpty()) null else selectedDate
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.createProductEntry(
                    "Bearer $token",
                    request
                )

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EntradasActivity,
                        response.body()?.get("message") ?: "Entrada registrada correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    limpiarCampos()
                } else {
                    val errorMessage = response.errorBody()?.string()

                    Log.e("ENTRADA_ERROR", "Código: ${response.code()} | Error: $errorMessage")

                    Toast.makeText(
                        this@EntradasActivity,
                        "Error ${response.code()}: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("ENTRADA_ERROR", "Error de conexión", e)

                Toast.makeText(
                    this@EntradasActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Función auxiliar que gestiona la lógica de `fechaEsValida`.
    private fun fechaEsValida(fecha: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false

            val fechaIngresada = sdf.parse(fecha)

            val hoy = Calendar.getInstance()
            hoy.set(Calendar.HOUR_OF_DAY, 0)
            hoy.set(Calendar.MINUTE, 0)
            hoy.set(Calendar.SECOND, 0)
            hoy.set(Calendar.MILLISECOND, 0)

            fechaIngresada != null && fechaIngresada.after(hoy.time)
        } catch (e: Exception) {
            false
        }
    }

    // Función auxiliar que gestiona la lógica de `limpiarCampos`.
    private fun limpiarCampos() {
        ingProducto.text.clear()
        ingCantidad.text.clear()
        ingLote.text.clear()
        ingVencimiento.text.clear()
        selectedDate = ""
        ingProducto.requestFocus()
    }
}
