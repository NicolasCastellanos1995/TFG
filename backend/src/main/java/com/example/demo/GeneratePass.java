package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase auxiliar utilizada para generar o comprobar contrasenas cifradas durante el desarrollo.
 */
public class GeneratePass {
    /**
     * Arranca la aplicacion Spring Boot.
     */
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("operario1234"));
    }
}





