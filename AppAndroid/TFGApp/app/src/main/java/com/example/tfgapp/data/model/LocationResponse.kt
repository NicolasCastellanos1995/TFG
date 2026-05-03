// Archivo comentado: app/src/main/java/com/example/tfgapp/data/model/LocationResponse.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.model

// Modelo de datos usado para enviar o recibir información de la API.
data class LocationResponse(
        val id: Long,
        val code: String,
        val name: String,
        val description: String?,
        val criticality: String?,
        val coordX: Double?,
        val coordY: Double?,
        val coordZ: Double?
) {
        override fun toString(): String {
                return name.ifBlank { code }
        }
}
