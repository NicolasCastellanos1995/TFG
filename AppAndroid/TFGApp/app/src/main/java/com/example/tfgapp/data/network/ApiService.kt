// Archivo comentado: app/src/main/java/com/example/tfgapp/data/network/ApiService.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.data.network

import com.example.tfgapp.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

// Contrato que define las operaciones disponibles, normalmente llamadas desde Retrofit.
interface ApiService {

    @POST("api/auth/login")
    // Función auxiliar que gestiona la lógica de `login`.
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/movements/entries")
    suspend fun createProductEntry(
        @Header("Authorization") token: String,
        @Body request: ProductEntryRequest
    ): Response<Map<String, String>>

    @GET("api/movements/users")
    // Función auxiliar que gestiona la lógica de `getUsers`.
    fun getUsers(
        @Header("Authorization") token: String
    ): Call<List<OperatorResponse>>

    @GET("api/movements/by-operator/{operatorId}")
    // Función auxiliar que gestiona la lógica de `getMovementsByOperator`.
    fun getMovementsByOperator(
        @Header("Authorization") token: String,
        @Path("operatorId") operatorId: Long
    ): Call<List<MovementResponse>>

    @GET("api/movements/by-location/{locationId}")
    // Función auxiliar que gestiona la lógica de `getMovementsByLocation`.
    fun getMovementsByLocation(
        @Header("Authorization") token: String,
        @Path("locationId") locationId: Long
    ): Call<List<MovementResponse>>

    @GET("api/alerts/low-stock")
    // Función auxiliar que gestiona la lógica de `getLowStockAlerts`.
    fun getLowStockAlerts(
        @Header("Authorization") token: String
    ): Call<List<AlertResponse>>

    @GET("api/alerts/expiring")
    // Función auxiliar que gestiona la lógica de `getExpiringAlerts`.
    fun getExpiringAlerts(
        @Header("Authorization") token: String,
        @Query("days") days: Int
    ): Call<List<AlertResponse>>
    @GET("api/products")
    suspend fun getProductsSuspend(
        @Header("Authorization") token: String
    ): Response<List<ProductResponse>>

    @GET("api/products")
    // Función auxiliar que gestiona la lógica de `getProducts`.
    fun getProducts(
        @Header("Authorization") token: String
    ): Call<List<ProductResponse>>

    @POST("api/categories")
    // Función auxiliar que gestiona la lógica de `createCategory`.
    fun createCategory(
        @Header("Authorization") token: String,
        @Body request: CategoryRequest
    ): Call<CategoryResponse>

    @GET("api/categories")
    // Función auxiliar que gestiona la lógica de `getCategories`.
    fun getCategories(
        @Header("Authorization") token: String
    ): Call<List<CategoryResponse>>

    @PUT("api/categories/{id}")
    // Función auxiliar que gestiona la lógica de `updateCategory`.
    fun updateCategory(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: CategoryRequest
    ): Call<CategoryResponse>

    @POST("api/products")
    // Función auxiliar que gestiona la lógica de `createProduct`.
    fun createProduct(
        @Header("Authorization") token: String,
        @Body request: ProductRequest
    ): Call<Void>

    @PUT("api/products/{id}")
    // Función auxiliar que gestiona la lógica de `updateProduct`.
    fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: ProductRequest
    ): Call<Void>

    @POST("api/locations")
    // Función auxiliar que gestiona la lógica de `createLocation`.
    fun createLocation(
        @Header("Authorization") token: String,
        @Body request: LocationRequest
    ): Call<Void>

    @GET("api/locations")
    // Función auxiliar que gestiona la lógica de `getLocations`.
    fun getLocations(
        @Header("Authorization") token: String
    ): Call<List<LocationResponse>>

    @POST("api/movements/exits")
    suspend fun createProductExit(
        @Header("Authorization") token: String,
        @Body request: ProductExitRequest
    ): Response<Map<String, String>>

    @GET("api/locations")
    suspend fun getLocationsSuspend(
        @Header("Authorization") token: String
    ): Response<List<LocationResponse>>
    @GET("api/locations/empty")
    suspend fun getEmptyLocationsSuspend(
        @Header("Authorization") token: String
    ): Response<List<LocationResponse>>

    @GET("api/locations/occupied")
    suspend fun getOccupiedLocationsSuspend(
        @Header("Authorization") token: String
    ): Response<List<LocationResponse>>
    @GET("api/stock/search")
    suspend fun searchStock(
        @Header("Authorization") token: String,
        @Query("location") location: String?,
        @Query("product") product: String?
    ): Response<List<StockResponse>>
    @POST("api/movements/transfers")
    suspend fun createTransfer(
        @Header("Authorization") token: String,
        @Body request: TransferRequest
    ): Response<Map<String, String>>
    @PUT("api/locations/{id}")
    // Función auxiliar que gestiona la lógica de `updateLocation`.
    fun updateLocation(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: LocationRequest
    ): Call<Void>
}
