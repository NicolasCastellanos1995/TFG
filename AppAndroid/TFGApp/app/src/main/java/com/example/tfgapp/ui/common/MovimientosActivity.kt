// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/common/MovimientosActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.common

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.LocationResponse
import com.example.tfgapp.data.model.MovementResponse
import com.example.tfgapp.data.model.OperatorResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class MovimientosActivity : AppCompatActivity() {

    private lateinit var spinnerOperario: Spinner
    private lateinit var spinnerUbicacion: Spinner
    private lateinit var listOperario: ListView
    private lateinit var listUbicacion: ListView

    private val operarioItems = mutableListOf<String>()
    private val ubicacionItems = mutableListOf<String>()

    private lateinit var operarioAdapter: ArrayAdapter<String>
    private lateinit var ubicacionAdapter: ArrayAdapter<String>

    private var token: String = ""

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        spinnerOperario = findViewById(R.id.spinnerOperario)
        spinnerUbicacion = findViewById(R.id.spinnerUbicacion)
        listOperario = findViewById(R.id.listMovimientosOperario)
        listUbicacion = findViewById(R.id.listMovimientosUbicacion)

        operarioAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            operarioItems
        )

        ubicacionAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            ubicacionItems
        )

        listOperario.adapter = operarioAdapter
        listUbicacion.adapter = ubicacionAdapter

        val rawToken = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("token", null)

        if (rawToken == null) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        token = "Bearer $rawToken"

        cargarOperarios()
        cargarUbicaciones()
    }

    // ==========================
    // OPERARIOS
    // ==========================

    // Función auxiliar que gestiona la lógica de `cargarOperarios`.
    private fun cargarOperarios() {
        RetrofitClient.api.getUsers(token)
            .enqueue(object : Callback<List<OperatorResponse>> {
                override fun onResponse(
                    call: Call<List<OperatorResponse>>,
                    response: Response<List<OperatorResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val operarios = mutableListOf(
                            OperatorResponse(-1L, "Selecciona operario")
                        )

                        operarios.addAll(response.body()!!)

                        val adapter = ArrayAdapter(
                            this@MovimientosActivity,
                            android.R.layout.simple_spinner_item,
                            operarios
                        )

                        adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        spinnerOperario.adapter = adapter

                        spinnerOperario.onItemSelectedListener =
                            // Objeto responsable de centralizar esta funcionalidad para reutilizarla desde varias pantallas.
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val selected = operarios[position]

                                    if (selected.id == -1L) {
                                        operarioItems.clear()
                                        operarioItems.add("Selecciona un operario")
                                        operarioAdapter.notifyDataSetChanged()
                                    } else {
                                        cargarMovimientosPorOperario(selected.id!!)
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }

                    } else {
                        Toast.makeText(
                            this@MovimientosActivity,
                            "Error cargando operarios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<OperatorResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@MovimientosActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // ==========================
    // UBICACIONES
    // ==========================

    // Función auxiliar que gestiona la lógica de `cargarUbicaciones`.
    private fun cargarUbicaciones() {
        RetrofitClient.api.getLocations(token)
            .enqueue(object : Callback<List<LocationResponse>> {
                override fun onResponse(
                    call: Call<List<LocationResponse>>,
                    response: Response<List<LocationResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val ubicaciones = mutableListOf(
                            LocationResponse(
                                id = -1L,
                                code = "Selecciona ubicación",
                                name = "Selecciona ubicación",
                                description = null,
                                criticality = null,
                                coordX = null,
                                coordY = null,
                                coordZ = null
                            )
                        )

                        ubicaciones.addAll(response.body()!!)

                        val adapter = ArrayAdapter(
                            this@MovimientosActivity,
                            android.R.layout.simple_spinner_item,
                            ubicaciones
                        )

                        adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        spinnerUbicacion.adapter = adapter

                        spinnerUbicacion.onItemSelectedListener =
                            // Objeto responsable de centralizar esta funcionalidad para reutilizarla desde varias pantallas.
                            object : AdapterView.OnItemSelectedListener {

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val selected = ubicaciones[position]

                                    if (selected.id == -1L) {
                                        ubicacionItems.clear()
                                        ubicacionItems.add("Selecciona una ubicación")
                                        ubicacionAdapter.notifyDataSetChanged()
                                    } else {
                                        cargarMovimientosPorUbicacion(selected.id!!)
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }

                    } else {
                        Toast.makeText(
                            this@MovimientosActivity,
                            "Error cargando ubicaciones",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@MovimientosActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // ==========================
    // MOVIMIENTOS
    // ==========================

    // Función auxiliar que gestiona la lógica de `cargarMovimientosPorOperario`.
    private fun cargarMovimientosPorOperario(id: Long) {
        RetrofitClient.api.getMovementsByOperator(token, id)
            .enqueue(object : Callback<List<MovementResponse>> {
                override fun onResponse(
                    call: Call<List<MovementResponse>>,
                    response: Response<List<MovementResponse>>
                ) {
                    operarioItems.clear()

                    if (response.isSuccessful && response.body() != null) {
                        val lista = response.body()!!

                        if (lista.isEmpty()) {
                            operarioItems.add("Sin movimientos")
                        } else {
                            lista.forEach {
                                operarioItems.add(formatearMovimiento(it))
                            }
                        }

                        operarioAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<MovementResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@MovimientosActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Función auxiliar que gestiona la lógica de `cargarMovimientosPorUbicacion`.
    private fun cargarMovimientosPorUbicacion(id: Long) {
        RetrofitClient.api.getMovementsByLocation(token, id)
            .enqueue(object : Callback<List<MovementResponse>> {
                override fun onResponse(
                    call: Call<List<MovementResponse>>,
                    response: Response<List<MovementResponse>>
                ) {
                    ubicacionItems.clear()

                    if (response.isSuccessful && response.body() != null) {
                        val lista = response.body()!!

                        if (lista.isEmpty()) {
                            ubicacionItems.add("Sin movimientos")
                        } else {
                            lista.forEach {
                                ubicacionItems.add(formatearMovimiento(it))
                            }
                        }

                        ubicacionAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<MovementResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@MovimientosActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // ==========================
    // FORMATEO
    // ==========================

    // Función auxiliar que gestiona la lógica de `formatearMovimiento`.
    private fun formatearMovimiento(m: MovementResponse): String {
        val tipo = m.typeLabel ?: m.type ?: "-"
        val producto = m.productName ?: "-"
        val cantidad = m.quantity ?: 0

        val ubicacion = when (m.type?.uppercase()) {
            "IN" -> "Destino: ${m.toLocationName ?: "-"}"
            "OUT" -> "Origen: ${m.fromLocationName ?: "-"}"
            "TRANSFER" -> "De: ${m.fromLocationName ?: "-"} → ${m.toLocationName ?: "-"}"
            else -> "-"
        }

        return "$tipo\n$producto ($cantidad)\n$ubicacion\nUsuario: ${m.username}"
    }
}
