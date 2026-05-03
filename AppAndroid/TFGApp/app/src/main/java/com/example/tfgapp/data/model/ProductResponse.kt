// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/ProductResponse.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model



// Modelo de datos usado para enviar o recibir información de la API.
data class ProductResponse(
        val id: Long,
        val sku: String,
        val name: String,
        val unit: String,
        val minStock: Int,
        val criticality: String,
        val categoryId: Long,
        val categoryName: String
)
