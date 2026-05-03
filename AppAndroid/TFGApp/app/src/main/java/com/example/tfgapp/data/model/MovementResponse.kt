// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/MovementResponse.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class MovementResponse(
    val id: Long?,
    val type: String?,
    val typeLabel: String?,
    val username: String?,
    val productName: String?,
    val quantity: Int?,
    val fromLocationName: String?,
    val toLocationName: String?,
    val createdAt: String?
)
