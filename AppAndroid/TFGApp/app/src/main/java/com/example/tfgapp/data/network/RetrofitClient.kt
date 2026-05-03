// Archivo comentado: app/src/main/java/com/example/tfgapp/data/network/RetrofitClient.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Objeto responsable de centralizar esta funcionalidad para reutilizarla desde varias pantallas.
object RetrofitClient {

    private const val PREFS_NAME = "server_config"
    private const val KEY_BASE_URL = "base_url"
    private const val DEFAULT_BASE_URL = "http://10.0.2.2:8080/"

    private var appContext: Context? = null
    private var apiService: ApiService? = null
    private var currentBaseUrl: String? = null

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
    )

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Guarda el contexto de la aplicación para poder acceder a preferencias compartidas.
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Crea o reutiliza el servicio de Retrofit según la URL base configurada.
    val api: ApiService
        get() {
            val baseUrl = getBaseUrl()
            if (apiService == null || currentBaseUrl != baseUrl) {
                apiService = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
                currentBaseUrl = baseUrl
            }
            return apiService!!
        }

    // Función auxiliar que gestiona la lógica de `getBaseUrl`.
    fun getBaseUrl(): String {
        val savedUrl = appContext
            ?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            ?.getString(KEY_BASE_URL, DEFAULT_BASE_URL)
            ?: DEFAULT_BASE_URL

        return normalizeBaseUrl(savedUrl)
    }

    // Función auxiliar que gestiona la lógica de `saveBaseUrl`.
    fun saveBaseUrl(context: Context, baseUrl: String) {
        val normalizedUrl = normalizeBaseUrl(baseUrl)
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_BASE_URL, normalizedUrl)
            .apply()

        appContext = context.applicationContext
        apiService = null
        currentBaseUrl = null
    }

    // Función auxiliar que gestiona la lógica de `normalizeBaseUrl`.
    private fun normalizeBaseUrl(url: String): String {
        var normalized = url.trim()

        if (normalized.isEmpty()) {
            normalized = DEFAULT_BASE_URL
        }

        if (!normalized.startsWith("http://") && !normalized.startsWith("https://")) {
            normalized = "http://$normalized"
        }

        if (!normalized.endsWith("/")) {
            normalized += "/"
        }

        return normalized
    }
}
