// Archivo comentado: app/src/main/java/com/example/tfgapp/ui/login/MainActivity.kt
// Explica la responsabilidad de esta clase y los pasos principales del flujo.

package com.example.tfgapp.ui.login

import android.app.Activity
import android.content.Intent
import com.example.tfgapp.ui.config.ServerConfigActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tfgapp.data.model.LoginRequest
import com.example.tfgapp.data.model.LoginResponse
import com.example.tfgapp.data.network.RetrofitClient
import com.example.tfgapp.databinding.ActivityMainBinding
import com.example.tfgapp.ui.home.OperarioActivity
import com.example.tfgapp.ui.home.SupervisorActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Clase principal de esta pantalla o componente de la aplicación.
class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    // Método que se ejecuta al abrir la pantalla: inicializa la vista y configura los botones.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitClient.init(applicationContext)

        binding.btnServerConfig.setOnClickListener {
            startActivity(Intent(this, ServerConfigActivity::class.java))
        }

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

    // Función auxiliar que gestiona la lógica de `doLogin`.
    private fun doLogin(username: String, password: String) {
        RetrofitClient.api.login(LoginRequest(username, password))
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        getSharedPreferences("auth", MODE_PRIVATE).edit()
                            .putString("token", loginResponse.token)
                            .putString("username", loginResponse.username)
                            .putString("role", loginResponse.role)
                            .apply()

                        val intent = when (loginResponse.role.uppercase()) {
                            "OPERARIO" -> Intent(this@MainActivity, OperarioActivity::class.java)
                            "SUPERVISOR" -> Intent(this@MainActivity, SupervisorActivity::class.java)
                            else -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Rol no reconocido",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                        }

                        Toast.makeText(
                            this@MainActivity,
                            "Bienvenida, ${loginResponse.username}",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(intent)
                        finish()
                    } else {
                        binding.tvResult.text = "Usuario o contraseña incorrectos"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.tvResult.text = "Error de conexión: ${t.message}"
                }
            })
    }
}
