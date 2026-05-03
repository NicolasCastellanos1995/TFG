package com.example.demo.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de ProductRequest.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
class ProductRequestTest {

    /**
     * Comprueba el caso de prueba: gettersAndSettersWorkCorrectly.
     */
    @Test
    void gettersAndSettersWorkCorrectly() {
        ProductRequest request = new ProductRequest();

        request.setSku("SKU-001");
        request.setName("Guantes");
        request.setCategoryId(1L);
        request.setUnit("unidad");
        request.setMinStock(10);
        request.setCriticality("ALTA");

        assertEquals("SKU-001", request.getSku());
        assertEquals("Guantes", request.getName());
        assertEquals(1L, request.getCategoryId());
        assertEquals("unidad", request.getUnit());
        assertEquals(10, request.getMinStock());
        assertEquals("ALTA", request.getCriticality());
    }
}
