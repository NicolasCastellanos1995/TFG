// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/ProductEntryRequest.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class ProductEntryRequest(
    val product: String,
    val quantity: Int,
    val lotCode: String,
    val expirationDate: String?
)
