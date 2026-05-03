package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion Spring Boot.
 * Inicializa el contexto de Spring y carga la configuracion necesaria
 * para ejecutar el sistema de inventario.
 */
@SpringBootApplication
public class TfgApplication {

    /**
     * Arranca la aplicacion Spring Boot.
     *
     * @param args argumentos recibidos por linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(TfgApplication.class, args);
    }
}