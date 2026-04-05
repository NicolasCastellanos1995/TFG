package com.example.tfgapp.data.network

import com.example.tfgapp.data.model.CategoriaRequest
import com.example.tfgapp.data.model.CategoriaResponse
import com.example.tfgapp.data.model.LoginRequest
import com.example.tfgapp.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @POST("api/categories")

    fun crearCategoria(
        @Body categoria: CategoriaRequest
    ): Call<CategoriaResponse>

}