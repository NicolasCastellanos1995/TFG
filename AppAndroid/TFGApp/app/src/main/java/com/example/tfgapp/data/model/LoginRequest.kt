// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/LoginRequest.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class LoginRequest(
    val username: String,
    val password: String
)
