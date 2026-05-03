package com.example.demo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de Stock.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
class StockTest {

    /**
     * Comprueba el caso de prueba: gettersAndSettersWorkCorrectly.
     */
    @Test
    void gettersAndSettersWorkCorrectly() {
        Stock stock = new Stock();
        Product product = new Product();
        Location location = new Location();
        LocalDate expirationDate = LocalDate.of(2026, 12, 31);

        stock.setId(1L);
        stock.setProduct(product);
        stock.setLocation(location);
        stock.setQuantity(15);
        stock.setLotCode("LOTE-01");
        stock.setExpirationDate(expirationDate);

        assertEquals(1L, stock.getId());
        assertSame(product, stock.getProduct());
        assertSame(location, stock.getLocation());
        assertEquals(15, stock.getQuantity());
        assertEquals("LOTE-01", stock.getLotCode());
        assertEquals(expirationDate, stock.getExpirationDate());
    }
}
