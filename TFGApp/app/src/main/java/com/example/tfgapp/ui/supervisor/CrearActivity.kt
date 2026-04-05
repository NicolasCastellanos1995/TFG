package com.example.tfgapp.ui.supervisor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R

class CrearActivity : AppCompatActivity() {

    private lateinit var btnCrearProducto: Button
    private lateinit var btnCrearUbicacion: Button
    private lateinit var btnCrearCategoria: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        btnCrearProducto = findViewById(R.id.btnCrearProducto)
        btnCrearUbicacion = findViewById(R.id.btnCrearUbicacion)
        btnCrearCategoria = findViewById(R.id.btnCrearCategoria)

        btnCrearProducto.setOnClickListener {
            val intent = Intent(this, CrearProductoActivity::class.java)
            startActivity(intent)
        }

        btnCrearUbicacion.setOnClickListener {
            val intent = Intent(this, CrearUbicacionActivity::class.java)
            startActivity(intent)
        }

        btnCrearCategoria.setOnClickListener {
            val intent = Intent(this, CrearCategoriaActivity::class.java)
            startActivity(intent)
        }
    }
}