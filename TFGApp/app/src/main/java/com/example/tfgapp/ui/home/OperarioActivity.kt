package com.example.tfgapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tfgapp.R
import com.example.tfgapp.ui.common.ConsultasStockActivity
import com.example.tfgapp.ui.common.EntradasActivity
import com.example.tfgapp.ui.common.HistorialMovimientosActivity
import com.example.tfgapp.ui.common.SalidasActivity
import com.example.tfgapp.ui.common.TransferenciasActivity

class OperarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operario)

        val btnEntradas = findViewById<Button>(R.id.btnEntradas)
        val btnSalidas = findViewById<Button>(R.id.btnSalidas)
        val btnTransferencias = findViewById<Button>(R.id.btnTransferencias)
        val btnConsultasStock = findViewById<Button>(R.id.btnConsultasStock)
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)

        btnEntradas.setOnClickListener {
            startActivity(Intent(this, EntradasActivity::class.java))
        }

        btnSalidas.setOnClickListener {
            startActivity(Intent(this, SalidasActivity::class.java))
        }

        btnTransferencias.setOnClickListener {
            startActivity(Intent(this, TransferenciasActivity::class.java))
        }

        btnConsultasStock.setOnClickListener {
            startActivity(Intent(this, ConsultasStockActivity::class.java))
        }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialMovimientosActivity::class.java))
        }
    }
}