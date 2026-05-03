// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/CrearActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.app.Activity
import com.example.tfgapp.R


// Clase principal de esta pantalla o componente de la aplicación.
class CrearActivity : Activity() {

    private lateinit var btnCrearProducto: Button
    private lateinit var btnCrearUbicacion: Button
    private lateinit var btnCrearCategoria: Button

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
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
