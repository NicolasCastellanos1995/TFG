package com.example.demo.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Componente que se ejecuta al iniciar la aplicacion.
// Su funcion es asegurar que exista la carpeta donde se guardan los logs.
/**
 * Componente encargado de crear la carpeta de logs al arrancar la aplicacion.
 * Su objetivo es evitar errores de escritura cuando Spring Boot intenta registrar trazas en fichero.
 */
@Component
public class LogDirectoryInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(LogDirectoryInitializer.class);

    // Carpeta de logs ubicada en la raiz del proyecto.
    static final Path LOG_DIRECTORY = Path.of("logs");

    @Override
    public void run(ApplicationArguments args) throws IOException {
        // Crea la carpeta si no existe. Si ya existe, no modifica su contenido.
        Files.createDirectories(LOG_DIRECTORY);

        // Registra en el log que la carpeta esta disponible.
        logger.info("Carpeta de logs preparada en: {}", LOG_DIRECTORY.toAbsolutePath());
    }
}
