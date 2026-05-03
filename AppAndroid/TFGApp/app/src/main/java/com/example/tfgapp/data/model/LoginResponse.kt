// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/LoginResponse.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class LoginResponse(
    val token: String,
    val username: String,
    val role: String,
    val redirectUrl: String
)
