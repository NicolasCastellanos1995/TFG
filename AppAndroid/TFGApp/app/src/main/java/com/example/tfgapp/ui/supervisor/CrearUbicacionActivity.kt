// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/CrearUbicacionActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.LocationRequest
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class CrearUbicacionActivity : AppCompatActivity() {

    private val criticidades = listOf("BAJA", "MEDIA", "ALTA")

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ubicacion)

        val etCodigo = findViewById<EditText>(R.id.etCodigo)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val autoCriticidad = findViewById<AutoCompleteTextView>(R.id.autoCriticidad)
        val etCoordX = findViewById<EditText>(R.id.etCoordX)
        val etCoordY = findViewById<EditText>(R.id.etCoordY)
        val etCoordZ = findViewById<EditText>(R.id.etCoordZ)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        configurarCriticidad(autoCriticidad)

        btnGuardar.setOnClickListener {
            val codigo = etCodigo.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val criticidad = autoCriticidad.text.toString().trim()
            val coordX = etCoordX.text.toString().trim().toDoubleOrNull()
            val coordY = etCoordY.text.toString().trim().toDoubleOrNull()
            val coordZ = etCoordZ.text.toString().trim().toDoubleOrNull()

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Introduce el código", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Introduce el nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (criticidad !in criticidades) {
                Toast.makeText(this, "Selecciona una criticidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (coordX == null || coordY == null || coordZ == null) {
                Toast.makeText(this, "Introduce coordenadas válidas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", null)

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnGuardar.isEnabled = false
            btnGuardar.text = "Guardando..."

            val request = LocationRequest(
                code = codigo,
                name = nombre,
                criticality = criticidad,
                coordX = coordX,
                coordY = coordY,
                coordZ = coordZ
            )

            RetrofitClient.api.createLocation("Bearer $token", request)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar ubicación"

                        if (response.isSuccessful) {
                            Toast.makeText(this@CrearUbicacionActivity, "Ubicación creada", Toast.LENGTH_SHORT).show()

                            etCodigo.setText("")
                            etNombre.setText("")
                            autoCriticidad.setText("", false)
                            etCoordX.setText("")
                            etCoordY.setText("")
                            etCoordZ.setText("")
                        } else {
                            Toast.makeText(this@CrearUbicacionActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar ubicación"
                        Toast.makeText(this@CrearUbicacionActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    // Función auxiliar que gestiona la lógica de `configurarCriticidad`.
    private fun configurarCriticidad(auto: AutoCompleteTextView) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, criticidades)
        auto.setAdapter(adapter)
        auto.threshold = 0
        auto.setOnClickListener { auto.showDropDown() }
        auto.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) auto.showDropDown()
        }
    }
}
