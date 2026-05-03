// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/AlertasActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.AlertResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class AlertasActivity : AppCompatActivity() {

    private lateinit var listStockMinimo: ListView
    private lateinit var listProximosVencer: ListView

    private val stockItems = mutableListOf<String>()
    private val vencimientoItems = mutableListOf<String>()

    private lateinit var stockAdapter: ArrayAdapter<String>
    private lateinit var vencimientoAdapter: ArrayAdapter<String>

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alertas)

        listStockMinimo = findViewById(R.id.listStockMinimo)
        listProximosVencer = findViewById(R.id.listProximosVencer)

        stockAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockItems)
        vencimientoAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vencimientoItems)

        listStockMinimo.adapter = stockAdapter
        listProximosVencer.adapter = vencimientoAdapter

        cargarAlertas()
    }

    // Función auxiliar que gestiona la lógica de `cargarAlertas`.
    private fun cargarAlertas() {
        val rawToken = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (rawToken == null) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            return
        }

        val token = "Bearer $rawToken"

        cargarStockMinimo(token)
        cargarProximosVencer(token)
    }

    // 🔴 STOCK MÍNIMO AGRUPADO POR PRODUCTO
    // Función auxiliar que gestiona la lógica de `cargarStockMinimo`.
    private fun cargarStockMinimo(token: String) {
        RetrofitClient.api.getLowStockAlerts(token)
            .enqueue(object : Callback<List<AlertResponse>> {
                override fun onResponse(
                    call: Call<List<AlertResponse>>,
                    response: Response<List<AlertResponse>>
                ) {
                    stockItems.clear()

                    if (response.isSuccessful && response.body() != null) {

                        val agrupado = response.body()!!
                            .groupBy { it.productSku }
                            .map { (_, items) ->
                                val primero = items.first()
                                val cantidadTotal = items.sumOf { it.quantity ?: 0 }
                                Triple(primero, cantidadTotal, primero.minStock ?: 0)
                            }
                            .filter { (_, cantidadTotal, minStock) ->
                                cantidadTotal < minStock
                            }

                        agrupado.forEach { (item, cantidadTotal, minStock) ->
                            stockItems.add(
                                "${item.productName ?: "Sin nombre"}\n" +
                                        "SKU: ${item.productSku ?: "-"}\n" +
                                        "Stock total: $cantidadTotal / Mínimo: $minStock"
                            )
                        }

                        if (stockItems.isEmpty()) {
                            stockItems.add("No hay productos debajo del stock mínimo")
                        }

                        stockAdapter.notifyDataSetChanged()

                    } else {
                        Toast.makeText(this@AlertasActivity, "Error al cargar stock mínimo", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<AlertResponse>>, t: Throwable) {
                    Toast.makeText(this@AlertasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 🟡 PRODUCTOS PRÓXIMOS A VENCER
    // Función auxiliar que gestiona la lógica de `cargarProximosVencer`.
    private fun cargarProximosVencer(token: String) {
        RetrofitClient.api.getExpiringAlerts(token, 7) // 👈 7 días (puedes cambiarlo)
            .enqueue(object : Callback<List<AlertResponse>> {
                override fun onResponse(
                    call: Call<List<AlertResponse>>,
                    response: Response<List<AlertResponse>>
                ) {
                    vencimientoItems.clear()

                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.forEach { item ->
                            vencimientoItems.add(
                                "${item.productName ?: "Sin nombre"}\n" +
                                        "SKU: ${item.productSku ?: "-"}\n" +
                                        "Lote: ${item.lotCode ?: "-"}\n" +
                                        "Vence: ${item.expirationDate ?: "-"}\n" +
                                        "Cantidad: ${item.quantity ?: 0}"
                            )
                        }

                        if (vencimientoItems.isEmpty()) {
                            vencimientoItems.add("No hay productos próximos a vencer")
                        }

                        vencimientoAdapter.notifyDataSetChanged()

                    } else {
                        Toast.makeText(this@AlertasActivity, "Error al cargar vencimientos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<AlertResponse>>, t: Throwable) {
                    Toast.makeText(this@AlertasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
