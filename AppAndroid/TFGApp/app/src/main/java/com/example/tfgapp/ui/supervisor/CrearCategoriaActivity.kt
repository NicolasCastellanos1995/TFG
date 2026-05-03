// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/CrearCategoriaActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.CategoryRequest
import com.example.tfgapp.data.model.CategoryResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class CrearCategoriaActivity : AppCompatActivity() {

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_categoria)

        val etNombre = findViewById<EditText>(R.id.etNombreCategoria)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCategoria)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Introduce un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("auth", MODE_PRIVATE)
            val token = prefs.getString("token", null)

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnGuardar.isEnabled = false
            btnGuardar.text = "Guardando..."

            RetrofitClient.api.createCategory(
                "Bearer $token",
                CategoryRequest(name = nombre)
            ).enqueue(object : Callback<CategoryResponse> {

                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    btnGuardar.isEnabled = true
                    btnGuardar.text = "Guardar"

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CrearCategoriaActivity,
                            "Categoría creada",
                            Toast.LENGTH_SHORT
                        ).show()

                        etNombre.setText("")
                    } else {
                        val error = response.errorBody()?.string()

                        Toast.makeText(
                            this@CrearCategoriaActivity,
                            "Error ${response.code()}: $error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    btnGuardar.isEnabled = true
                    btnGuardar.text = "Guardar"

                    Toast.makeText(
                        this@CrearCategoriaActivity,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}
