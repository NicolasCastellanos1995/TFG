// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/EditarUbicacionActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.LocationRequest
import com.example.tfgapp.data.model.LocationResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class EditarUbicacionActivity : AppCompatActivity() {

    private var ubicaciones: List<LocationResponse> = emptyList()
    private var ubicacionSeleccionada: LocationResponse? = null

    private val criticidades = listOf("BAJA", "MEDIA", "ALTA")

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_ubicacion)

        val autoUbicacion = findViewById<AutoCompleteTextView>(R.id.autoUbicacionExistente)
        val etCodigo = findViewById<EditText>(R.id.etEditarCodigo)
        val etNombre = findViewById<EditText>(R.id.etEditarNombre)
        val autoCriticidad = findViewById<AutoCompleteTextView>(R.id.autoEditarCriticidad)
        val etX = findViewById<EditText>(R.id.etEditarCoordX)
        val etY = findViewById<EditText>(R.id.etEditarCoordY)
        val etZ = findViewById<EditText>(R.id.etEditarCoordZ)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarUbicacionEditada)

        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
            return
        }

        configurarCriticidad(autoCriticidad)
        cargarUbicaciones(token, autoUbicacion, etCodigo, etNombre, autoCriticidad, etX, etY, etZ)

        btnGuardar.setOnClickListener {
            val ubicacion = ubicacionSeleccionada

            if (ubicacion == null) {
                Toast.makeText(this, "Selecciona una ubicación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val codigo = etCodigo.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val criticidad = autoCriticidad.text.toString().trim()
            val x = etX.text.toString().trim().toDoubleOrNull()
            val y = etY.text.toString().trim().toDoubleOrNull()
            val z = etZ.text.toString().trim().toDoubleOrNull()

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

            if (x == null || y == null || z == null) {
                Toast.makeText(this, "Introduce coordenadas válidas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LocationRequest(
                code = codigo,
                name = nombre,
                criticality = criticidad,
                coordX = x,
                coordY = y,
                coordZ = z
            )

            btnGuardar.isEnabled = false
            btnGuardar.text = "Guardando..."

            RetrofitClient.api.updateLocation("Bearer $token", ubicacion.id, request)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar cambios"

                        if (response.isSuccessful) {
                            Toast.makeText(this@EditarUbicacionActivity, "Ubicación actualizada", Toast.LENGTH_SHORT).show()
                            cargarUbicaciones(token, autoUbicacion, etCodigo, etNombre, autoCriticidad, etX, etY, etZ)
                        } else {
                            Toast.makeText(this@EditarUbicacionActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar cambios"
                        Toast.makeText(this@EditarUbicacionActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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

    // Función auxiliar que gestiona la lógica de `cargarUbicaciones`.
    private fun cargarUbicaciones(
        token: String,
        auto: AutoCompleteTextView,
        etCodigo: EditText,
        etNombre: EditText,
        autoCriticidad: AutoCompleteTextView,
        etX: EditText,
        etY: EditText,
        etZ: EditText
    ) {
        RetrofitClient.api.getLocations("Bearer $token")
            .enqueue(object : Callback<List<LocationResponse>> {
                override fun onResponse(
                    call: Call<List<LocationResponse>>,
                    response: Response<List<LocationResponse>>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(this@EditarUbicacionActivity, "Error API: ${response.code()}", Toast.LENGTH_LONG).show()
                        return
                    }

                    ubicaciones = response.body() ?: emptyList()

                    val adapter = ArrayAdapter(
                        this@EditarUbicacionActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        ubicaciones.map { "${it.code} - ${it.name}" }
                    )

                    auto.setAdapter(adapter)
                    auto.threshold = 0
                    auto.setOnClickListener { auto.showDropDown() }
                    auto.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) auto.showDropDown()
                    }

                    auto.setOnItemClickListener { _, _, position, _ ->
                        val u = ubicaciones[position]
                        ubicacionSeleccionada = u

                        etCodigo.setText(u.code)
                        etNombre.setText(u.name)
                        autoCriticidad.setText(u.criticality, false)
                        etX.setText(u.coordX.toString())
                        etY.setText(u.coordY.toString())
                        etZ.setText(u.coordZ.toString())
                    }
                }

                override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                    Toast.makeText(this@EditarUbicacionActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
