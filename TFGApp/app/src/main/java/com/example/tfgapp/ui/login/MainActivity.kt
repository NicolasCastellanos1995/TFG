package com.example.tfgapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.data.model.ErrorResponse
import com.example.tfgapp.data.model.LoginRequest
import com.example.tfgapp.data.model.LoginResponse
import com.example.tfgapp.data.network.RetrofitClient
import com.example.tfgapp.databinding.ActivityMainBinding
import com.example.tfgapp.ui.home.SupervisorActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            doLogin(username, password)
        }
    }

    private fun doLogin(username: String, password: String) {
        val request = LoginRequest(username, password)

        RetrofitClient.api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                    prefs.edit()
                        .putString("token", loginResponse.token)
                        .putString("username", loginResponse.username)
                        .putString("role", loginResponse.role)
                        .apply()

                    Toast.makeText(
                        this@MainActivity,
                        "Bienvenida, ${loginResponse.username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // SELECTOR DE UI
                    val intent = when (loginResponse.role.uppercase()) {
                        "OPERARIO" -> Intent(this@MainActivity, SupervisorActivity::class.java)
                        "SUPERVISOR" -> Intent(this@MainActivity, SupervisorActivity::class.java)
                        else ->  {Toast.makeText(this@MainActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                            return
                    } }

                    startActivity(intent)
                    finish()
                } else {
                    val errorJson = response.errorBody()?.string()

                    try {
                        val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
                        binding.tvResult.text = errorResponse.message
                    } catch (e: Exception) {
                        binding.tvResult.text = "Login incorrecto"
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.tvResult.text = "Error: ${t.message}"
            }
        })
    }
}