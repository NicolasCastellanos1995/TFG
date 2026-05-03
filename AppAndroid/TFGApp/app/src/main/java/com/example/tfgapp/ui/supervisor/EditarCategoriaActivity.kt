// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/EditarCategoriaActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.CategoryRequest
import com.example.tfgapp.data.model.CategoryResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class EditarCategoriaActivity : AppCompatActivity() {

    private var categorias: List<CategoryResponse> = emptyList()
    private var categoriaSeleccionada: CategoryResponse? = null

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_categoria)

        val autoCategoria = findViewById<AutoCompleteTextView>(R.id.autoCategoriaExistente)
        val etNuevoNombre = findViewById<EditText>(R.id.etNuevoNombreCategoria)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambiosCategoria)

        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
            return
        }

        cargarCategorias(token, autoCategoria, etNuevoNombre)

        btnGuardar.setOnClickListener {
            val categoria = categoriaSeleccionada

            if (categoria == null) {
                Toast.makeText(this, "Selecciona una categoría", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoNombre = etNuevoNombre.text.toString().trim()

            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "Introduce el nuevo nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnGuardar.isEnabled = false
            btnGuardar.text = "Guardando..."

            val request = CategoryRequest(name = nuevoNombre)

            RetrofitClient.api.updateCategory("Bearer $token", categoria.id, request)
                .enqueue(object : Callback<CategoryResponse> {

                    override fun onResponse(
                        call: Call<CategoryResponse>,
                        response: Response<CategoryResponse>
                    ) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar cambios"

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@EditarCategoriaActivity,
                                "Categoría actualizada",
                                Toast.LENGTH_SHORT
                            ).show()

                            categoriaSeleccionada = null
                            autoCategoria.setText("")
                            etNuevoNombre.setText("")

                            cargarCategorias(token, autoCategoria, etNuevoNombre)
                        } else {
                            Toast.makeText(
                                this@EditarCategoriaActivity,
                                "Error al actualizar: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar cambios"

                        Toast.makeText(
                            this@EditarCategoriaActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    // Función auxiliar que gestiona la lógica de `cargarCategorias`.
    private fun cargarCategorias(
        token: String,
        autoCategoria: AutoCompleteTextView,
        etNuevoNombre: EditText
    ) {
        RetrofitClient.api.getCategories("Bearer $token")
            .enqueue(object : Callback<List<CategoryResponse>> {

                override fun onResponse(
                    call: Call<List<CategoryResponse>>,
                    response: Response<List<CategoryResponse>>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@EditarCategoriaActivity,
                            "Error API categorías: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    categorias = response.body() ?: emptyList()

                    val adapter = ArrayAdapter(
                        this@EditarCategoriaActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        categorias.map { it.name }
                    )

                    autoCategoria.setAdapter(adapter)
                    autoCategoria.threshold = 0

                    autoCategoria.setOnClickListener {
                        autoCategoria.showDropDown()
                    }

                    autoCategoria.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) autoCategoria.showDropDown()
                    }

                    autoCategoria.setOnItemClickListener { _, _, position, _ ->
                        val categoria = categorias[position]
                        categoriaSeleccionada = categoria
                        etNuevoNombre.setText(categoria.name)
                    }
                }

                override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@EditarCategoriaActivity,
                        "Error cargando categorías: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
