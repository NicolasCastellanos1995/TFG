package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

/**
 * Pruebas unitarias de LogDirectoryInitializer.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
class LogDirectoryInitializerTest {

    /**
     * Comprueba el caso de prueba: createsLogsDirectoryWhenApplicationStarts.
     */
    @Test
    void createsLogsDirectoryWhenApplicationStarts() throws Exception {
        LogDirectoryInitializer initializer = new LogDirectoryInitializer();

        initializer.run(new DefaultApplicationArguments());

        assertTrue(Files.exists(Path.of("logs")));
        assertTrue(Files.isDirectory(Path.of("logs")));
    }
}
