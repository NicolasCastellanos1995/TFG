package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de TfgApplications.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
class TfgApplicationTests {

    /**
     * Comprueba el caso de prueba: mainMethodExists.
     */
    @Test
    void mainMethodExists() {
        assertDoesNotThrow(() -> TfgApplication.class.getDeclaredMethod("main", String[].class));
    }
}
