// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/supervisor/EditarActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.supervisor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.app.Activity
import com.example.tfgapp.R

// Clase principal de esta pantalla o componente de la aplicación.
class EditarActivity : Activity() {
    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)

        findViewById<Button>(R.id.btnEditarProducto).setOnClickListener {
            startActivity(Intent(this, EditarProductoActivity::class.java))
        }
        findViewById<Button>(R.id.btnEditarUbicacion).setOnClickListener {
            startActivity(Intent(this, EditarUbicacionActivity::class.java))
        }
        findViewById<Button>(R.id.btnEditarCategoria).setOnClickListener {
            startActivity(Intent(this, EditarCategoriaActivity::class.java))
        }
    }
}
