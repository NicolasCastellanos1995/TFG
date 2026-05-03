// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/AlertResponse.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class AlertResponse(
    val productSku: String?,
    val productName: String?,
    val locationCode: String?,
    val locationName: String?,
    val quantity: Int?,
    val minStock: Int?,
    val lotCode: String?,
    val expirationDate: String?
)
