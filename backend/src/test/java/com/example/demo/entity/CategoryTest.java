package com.example.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de Category.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
class CategoryTest {

    /**
     * Comprueba el caso de prueba: gettersAndSettersWorkCorrectly.
     */
    @Test
    void gettersAndSettersWorkCorrectly() {
        Category category = new Category();

        category.setId(1L);
        category.setName("Material sanitario");

        assertEquals(1L, category.getId());
        assertEquals("Material sanitario", category.getName());
        assertNotNull(category.getProducts());
    }
}
