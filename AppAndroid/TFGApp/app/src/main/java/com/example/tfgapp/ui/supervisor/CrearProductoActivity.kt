// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/CrearProductoActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.CategoryResponse
import com.example.tfgapp.data.model.ProductRequest
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class CrearProductoActivity : AppCompatActivity() {

    private var categorias: List<CategoryResponse> = emptyList()
    private var categoriaSeleccionada: CategoryResponse? = null

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        val etSku = findViewById<EditText>(R.id.etSku)
        val etNombreProducto = findViewById<EditText>(R.id.etNombreProducto)
        val autoCategoria = findViewById<AutoCompleteTextView>(R.id.autoCategoriaProducto)
        val autoCriticidad = findViewById<AutoCompleteTextView>(R.id.autoCriticidadProducto)
        val etUnidad = findViewById<EditText>(R.id.etUnidad)
        val etStockMinimo = findViewById<EditText>(R.id.etStockMinimo)
        val btnGuardarProducto = findViewById<Button>(R.id.btnGuardarProducto)

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = prefs.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔥 CONFIGURAR CRITICIDAD
        val criticidades = listOf("BAJA", "MEDIA", "ALTA")

        val criticidadAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            criticidades
        )

        autoCriticidad.setAdapter(criticidadAdapter)
        autoCriticidad.threshold = 0

        autoCriticidad.setOnClickListener {
            autoCriticidad.showDropDown()
        }

        autoCriticidad.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) autoCriticidad.showDropDown()
        }

        // 🔥 CARGAR CATEGORÍAS
        cargarCategorias(token, autoCategoria)

        btnGuardarProducto.setOnClickListener {

            val sku = etSku.text.toString().trim()
            val nombre = etNombreProducto.text.toString().trim()
            val criticidad = autoCriticidad.text.toString().trim()
            val unidad = etUnidad.text.toString().trim()
            val stockMinimo = etStockMinimo.text.toString().trim().toIntOrNull()

            if (sku.isEmpty()) {
                Toast.makeText(this, "Introduce el SKU", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Introduce el nombre del producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (criticidad !in criticidades) {
                Toast.makeText(this, "Selecciona una criticidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (categoriaSeleccionada == null) {
                Toast.makeText(this, "Selecciona una categoría", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (unidad.isEmpty()) {
                Toast.makeText(this, "Introduce la unidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (stockMinimo == null) {
                Toast.makeText(this, "Introduce un stock mínimo válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnGuardarProducto.isEnabled = false
            btnGuardarProducto.text = "Guardando..."

            val request = ProductRequest(
                sku = sku,
                name = nombre,
                categoryId = categoriaSeleccionada!!.id,
                unit = unidad,
                minStock = stockMinimo,
                criticality = criticidad // 🔥 AÑADIDO
            )

            RetrofitClient.api.createProduct("Bearer $token", request)
                .enqueue(object : Callback<Void> {

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        btnGuardarProducto.isEnabled = true
                        btnGuardarProducto.text = "Guardar producto"

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CrearProductoActivity,
                                "Producto creado",
                                Toast.LENGTH_SHORT
                            ).show()

                            etSku.setText("")
                            etNombreProducto.setText("")
                            autoCategoria.setText("")
                            autoCriticidad.setText("")
                            etUnidad.setText("")
                            etStockMinimo.setText("")
                            categoriaSeleccionada = null
                        } else {
                            Toast.makeText(
                                this@CrearProductoActivity,
                                "Error al crear producto: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        btnGuardarProducto.isEnabled = true
                        btnGuardarProducto.text = "Guardar producto"

                        Toast.makeText(
                            this@CrearProductoActivity,
                            "Error de conexión: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    // Función auxiliar que gestiona la lógica de `cargarCategorias`.
    private fun cargarCategorias(token: String, autoCategoria: AutoCompleteTextView) {
        RetrofitClient.api.getCategories("Bearer $token")
            .enqueue(object : Callback<List<CategoryResponse>> {

                override fun onResponse(
                    call: Call<List<CategoryResponse>>,
                    response: Response<List<CategoryResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        categorias = response.body()!!

                        val nombresCategorias = categorias.map { it.name }

                        val adapter = ArrayAdapter(
                            this@CrearProductoActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            nombresCategorias
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
                            categoriaSeleccionada = categorias[position]
                        }
                    } else {
                        Toast.makeText(
                            this@CrearProductoActivity,
                            "Error al cargar categorías: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@CrearProductoActivity,
                        "Error cargando categorías: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
