package com.example.tfgapp.ui.supervisor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.data.model.CategoriaRequest
import com.example.tfgapp.data.model.CategoriaResponse
import com.example.tfgapp.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearCategoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_categoria)

        val etNombre = findViewById<EditText>(R.id.etNombreCategoria)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCategoria)

        btnGuardar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()

            if (nombre.isEmpty()) {
                etNombre.error = "Introduce un nombre"
                return@setOnClickListener
            }

            val categoria = CategoriaRequest(name = nombre)

            RetrofitClient.api.crearCategoria(categoria)
                .enqueue(object : Callback<CategoriaResponse> {

                    override fun onResponse(
                        call: Call<CategoriaResponse>,
                        response: Response<CategoriaResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CrearCategoriaActivity,
                                "Categoría creada",
                                Toast.LENGTH_SHORT
                            ).show()

                            etNombre.text.clear()
                        } else {
                            Toast.makeText(
                                this@CrearCategoriaActivity,
                                "Error en el servidor",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CategoriaResponse>, t: Throwable) {
                        Toast.makeText(
                            this@CrearCategoriaActivity,
                            "Error de conexión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}