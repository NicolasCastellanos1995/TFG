package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;

/**
 * Pruebas unitarias de StockService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private StockService stockService;

    /**
     * Comprueba el caso de prueba: getTotalStockByProductSumsQuantitiesAndTreatsNullAsZero.
     */
    @Test
    void getTotalStockByProductSumsQuantitiesAndTreatsNullAsZero() {
        Product product = new Product();
        product.setId(1L);

        Stock stock1 = new Stock();
        stock1.setQuantity(10);

        Stock stock2 = new Stock();
        stock2.setQuantity(null);

        Stock stock3 = new Stock();
        stock3.setQuantity(7);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(stockRepository.findByProduct(product)).thenReturn(List.of(stock1, stock2, stock3));

        Integer total = stockService.getTotalStockByProduct(1L);

        assertEquals(17, total);
    }

    /**
     * Comprueba el caso de prueba: getTotalStockByProductThrowsExceptionWhenProductDoesNotExist.
     */
    @Test
    void getTotalStockByProductThrowsExceptionWhenProductDoesNotExist() {
        when(productRepository.findById(50L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stockService.getTotalStockByProduct(50L));

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    /**
     * Comprueba el caso de prueba: getAllStockReturnsRepositoryResult.
     */
    @Test
    void getAllStockReturnsRepositoryResult() {
        Stock stock = new Stock();
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        List<Stock> result = stockService.getAllStock();

        assertEquals(1, result.size());
        assertTrue(result.contains(stock));
    }
}
