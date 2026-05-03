// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/EditarProductoActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.CategoryResponse
import com.example.tfgapp.data.model.ProductRequest
import com.example.tfgapp.data.model.ProductResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class EditarProductoActivity : AppCompatActivity() {

    private var productos: List<ProductResponse> = emptyList()
    private var categorias: List<CategoryResponse> = emptyList()

    private var productoSeleccionado: ProductResponse? = null
    private var categoriaSeleccionada: CategoryResponse? = null

    private val criticidades = listOf("BAJA", "MEDIA", "ALTA")

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        val autoProducto = findViewById<AutoCompleteTextView>(R.id.autoProductoExistente)
        val etSku = findViewById<EditText>(R.id.etEditarSku)
        val etNombre = findViewById<EditText>(R.id.etEditarNombreProducto)
        val autoCriticidad = findViewById<AutoCompleteTextView>(R.id.autoEditarCriticidadProducto)
        val autoCategoria = findViewById<AutoCompleteTextView>(R.id.autoEditarCategoriaProducto)
        val etUnidad = findViewById<EditText>(R.id.etEditarUnidad)
        val etStockMinimo = findViewById<EditText>(R.id.etEditarStockMinimo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarProductoEditado)

        val token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
            return
        }

        configurarCriticidad(autoCriticidad)
        cargarCategorias(token, autoCategoria)
        cargarProductos(token, autoProducto, etSku, etNombre, autoCriticidad, autoCategoria, etUnidad, etStockMinimo)

        btnGuardar.setOnClickListener {
            val producto = productoSeleccionado

            if (producto == null) {
                Toast.makeText(this, "Selecciona un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sku = etSku.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val criticidad = autoCriticidad.text.toString().trim()
            val unidad = etUnidad.text.toString().trim()
            val stockMinimo = etStockMinimo.text.toString().trim().toIntOrNull()

            if (sku.isEmpty()) {
                Toast.makeText(this, "Introduce el SKU", Toast.LENGTH_SHORT).show()
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

            val request = ProductRequest(
                sku = sku,
                name = nombre,
                categoryId = categoriaSeleccionada!!.id,
                unit = unidad,
                minStock = stockMinimo,
                criticality = criticidad
            )

            btnGuardar.isEnabled = false
            btnGuardar.text = "Guardando..."

            RetrofitClient.api.updateProduct("Bearer $token", producto.id, request)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar cambios"

                        if (response.isSuccessful) {
                            Toast.makeText(this@EditarProductoActivity, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@EditarProductoActivity, "Error al actualizar: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        btnGuardar.isEnabled = true
                        btnGuardar.text = "Guardar cambios"
                        Toast.makeText(this@EditarProductoActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    // Función auxiliar que gestiona la lógica de `configurarCriticidad`.
    private fun configurarCriticidad(autoCriticidad: AutoCompleteTextView) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, criticidades)

        autoCriticidad.setAdapter(adapter)
        autoCriticidad.threshold = 0

        autoCriticidad.setOnClickListener {
            autoCriticidad.showDropDown()
        }

        autoCriticidad.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) autoCriticidad.showDropDown()
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

                        val adapter = ArrayAdapter(
                            this@EditarProductoActivity,
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
                            categoriaSeleccionada = categorias[position]
                        }
                    } else {
                        Toast.makeText(
                            this@EditarProductoActivity,
                            "Error API categorías: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@EditarProductoActivity,
                        "Error cargando categorías: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    // Función auxiliar que gestiona la lógica de `cargarProductos`.
    private fun cargarProductos(
        token: String,
        autoProducto: AutoCompleteTextView,
        etSku: EditText,
        etNombre: EditText,
        autoCriticidad: AutoCompleteTextView,
        autoCategoria: AutoCompleteTextView,
        etUnidad: EditText,
        etStockMinimo: EditText
    ) {
        RetrofitClient.api.getProducts("Bearer $token")
            .enqueue(object : Callback<List<ProductResponse>> {

                override fun onResponse(
                    call: Call<List<ProductResponse>>,
                    response: Response<List<ProductResponse>>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@EditarProductoActivity,
                            "Error API productos: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    productos = response.body() ?: emptyList()

                    val adapter = ArrayAdapter(
                        this@EditarProductoActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        productos.map { "${it.sku} - ${it.name}" }
                    )

                    autoProducto.setAdapter(adapter)
                    autoProducto.threshold = 0

                    autoProducto.setOnClickListener {
                        autoProducto.showDropDown()
                    }

                    autoProducto.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) autoProducto.showDropDown()
                    }

                    autoProducto.setOnItemClickListener { _, _, position, _ ->
                        val producto = productos[position]
                        productoSeleccionado = producto

                        etSku.setText(producto.sku)
                        etNombre.setText(producto.name)
                        autoCriticidad.setText(producto.criticality, false)
                        etUnidad.setText(producto.unit)
                        etStockMinimo.setText(producto.minStock.toString())

                        val categoria = categorias.find { it.id == producto.categoryId }
                        categoriaSeleccionada = categoria

                        autoCategoria.setText(producto.categoryName, false)
                    }
                }

                override fun onFailure(call: Call<List<ProductResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@EditarProductoActivity,
                        "Error cargando productos: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
