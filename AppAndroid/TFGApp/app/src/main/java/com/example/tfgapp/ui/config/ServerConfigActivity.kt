// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/config/ServerConfigActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.config

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.example.tfgapp.data.network.RetrofitClient
import com.example.tfgapp.databinding.ActivityServerConfigBinding

// Clase principal de esta pantalla o componente de la aplicación.
class ServerConfigActivity : Activity() {

    private lateinit var binding: ActivityServerConfigBinding

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitClient.init(applicationContext)
        binding.etServerUrl.setText(RetrofitClient.getBaseUrl())

        binding.btnSaveServerUrl.setOnClickListener {
            val url = binding.etServerUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(this, "Introduce la URL del servidor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.saveBaseUrl(this, url)
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnCancelServerConfig.setOnClickListener {
            finish()
        }
    }
}
