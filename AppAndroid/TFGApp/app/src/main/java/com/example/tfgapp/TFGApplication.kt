// Archivo comentado: app/src/main/java/com/example/tfgapp/TFGApplication.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp

import android.app.Application
import com.example.tfgapp.data.network.RetrofitClient

// Clase principal de esta pantalla o componente de la aplicación.
class TFGApplication : Application() {
    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.init(this)
    }
}
