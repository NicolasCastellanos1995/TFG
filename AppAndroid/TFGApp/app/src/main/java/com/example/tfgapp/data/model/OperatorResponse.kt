// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/OperatorResponse.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class OperatorResponse(
        val id: Long?,
        val username: String?
) {
        override fun toString(): String {
                return username ?: "Sin nombre"
        }
}
