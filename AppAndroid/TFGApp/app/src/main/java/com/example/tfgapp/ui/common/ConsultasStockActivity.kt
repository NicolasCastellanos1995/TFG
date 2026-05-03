// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/common/ConsultasStockActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.common

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfgapp.R
import com.example.tfgapp.data.model.LocationResponse
import com.example.tfgapp.data.model.ProductResponse
import com.example.tfgapp.data.model.StockResponse
import com.example.tfgapp.data.network.RetrofitClient
import kotlinx.coroutines.launch

// Clase principal de esta pantalla o componente de la aplicación.
class ConsultasStockActivity : AppCompatActivity() {

    private lateinit var etUbicacionStock: AutoCompleteTextView
    private lateinit var etProductoStock: AutoCompleteTextView
    private lateinit var btnConsultarStock: Button
    private lateinit var txtResultadoStock: TextView

    private var locations: List<LocationResponse> = emptyList()
    private var products: List<ProductResponse> = emptyList()

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas_stock)

        etUbicacionStock = findViewById(R.id.etUbicacionStock)
        etProductoStock = findViewById(R.id.etProductoStock)
        btnConsultarStock = findViewById(R.id.btnConsultarStock)
        txtResultadoStock = findViewById(R.id.txtResultadoStock)

        cargarDatos()

        btnConsultarStock.setOnClickListener {
            consultarStock()
        }
    }

    // Función auxiliar que gestiona la lógica de `cargarDatos`.
    private fun cargarDatos() {
        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val locationsResponse = RetrofitClient.api.getLocationsSuspend("Bearer $token")
                val productsResponse = RetrofitClient.api.getProductsSuspend("Bearer $token")

                if (locationsResponse.isSuccessful && productsResponse.isSuccessful) {

                    locations = locationsResponse.body() ?: emptyList()
                    products = productsResponse.body() ?: emptyList()

                    val locationItems = locations.map { "${it.code} - ${it.name}" }
                    val productItems = products.map { "${it.sku} - ${it.name}" }

                    etUbicacionStock.setAdapter(
                        ArrayAdapter(
                            this@ConsultasStockActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            locationItems
                        )
                    )

                    etProductoStock.setAdapter(
                        ArrayAdapter(
                            this@ConsultasStockActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            productItems
                        )
                    )

                    etUbicacionStock.setOnClickListener { etUbicacionStock.showDropDown() }
                    etProductoStock.setOnClickListener { etProductoStock.showDropDown() }

                } else {
                    val locError = locationsResponse.errorBody()?.string()
                    val prodError = productsResponse.errorBody()?.string()

                    Log.e("STOCK_ERROR", "Locations ${locationsResponse.code()}: $locError")
                    Log.e("STOCK_ERROR", "Products ${productsResponse.code()}: $prodError")

                    Toast.makeText(this@ConsultasStockActivity, "Error cargando datos", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("STOCK_ERROR", "Error cargando datos", e)
                Toast.makeText(this@ConsultasStockActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función auxiliar que gestiona la lógica de `consultarStock`.
    private fun consultarStock() {

        val ubicacionText = etUbicacionStock.text.toString().trim()
        val productoText = etProductoStock.text.toString().trim()

        // 🔹 Validaciones
        if (ubicacionText.isEmpty() && productoText.isEmpty()) {
            Toast.makeText(this, "Introduce ubicación o producto", Toast.LENGTH_SHORT).show()
            return
        }

        if (ubicacionText.isNotEmpty() && productoText.isNotEmpty()) {
            Toast.makeText(this, "Busca por ubicación o por producto, no ambos", Toast.LENGTH_SHORT).show()
            return
        }

        val locationSearch = if (ubicacionText.isNotEmpty()) {
            ubicacionText.substringBefore(" - ").trim()
        } else null

        val productSearch = if (productoText.isNotEmpty()) {
            productoText.substringBefore(" - ").trim()
        } else null

        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.searchStock(
                    "Bearer $token",
                    locationSearch,
                    productSearch
                )

                if (response.isSuccessful) {

                    val stockList: List<StockResponse> = response.body() ?: emptyList()

                    if (stockList.isEmpty()) {
                        txtResultadoStock.text = "No se encontró stock"
                        return@launch
                    }

                    txtResultadoStock.text = stockList.joinToString("\n\n") { stock ->
                        """
                        Producto: ${stock.productSku} - ${stock.productName}
                        Ubicación: ${stock.locationCode} - ${stock.locationName}
                        Stock: ${stock.quantity}
                        """.trimIndent()
                    }

                } else {
                    val error = response.errorBody()?.string()
                    Log.e("STOCK_ERROR", "Error ${response.code()}: $error")
                    txtResultadoStock.text = "Error consultando stock"
                }

            } catch (e: Exception) {
                Log.e("STOCK_ERROR", "Error consultando stock", e)
                Toast.makeText(
                    this@ConsultasStockActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
