package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.LowStockAlertResponse;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;

/**
 * Pruebas unitarias de AlertService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AlertService alertService;

    /**
     * Comprueba el caso de prueba: getLowStockProductsReturnsOnlyProductsWithTotalQuantityLowerOrEqualThanMinimum.
     */
    @Test
    void getLowStockProductsReturnsOnlyProductsWithTotalQuantityLowerOrEqualThanMinimum() {
        Product lowProduct = new Product();
        lowProduct.setId(1L);
        lowProduct.setSku("LOW-1");
        lowProduct.setName("Producto bajo");
        lowProduct.setMinStock(10);

        Product okProduct = new Product();
        okProduct.setId(2L);
        okProduct.setSku("OK-1");
        okProduct.setName("Producto correcto");
        okProduct.setMinStock(5);

        Stock lowStock = new Stock();
        lowStock.setProduct(lowProduct);
        lowStock.setQuantity(8);

        Stock okStock = new Stock();
        okStock.setProduct(okProduct);
        okStock.setQuantity(9);

        when(productRepository.findAll()).thenReturn(List.of(lowProduct, okProduct));
        when(stockRepository.findAll()).thenReturn(List.of(lowStock, okStock));

        List<LowStockAlertResponse> result = alertService.getLowStockProducts();

        assertEquals(1, result.size());
        assertEquals("LOW-1", result.get(0).getProductSku());
        assertEquals("Producto bajo", result.get(0).getProductName());
        assertEquals(8, result.get(0).getQuantity());
        assertEquals(10, result.get(0).getMinStock());
    }

    /**
     * Comprueba el caso de prueba: getExpiringProductsReturnsStocksExpiringOnOrBeforeLimitDate.
     */
    @Test
    void getExpiringProductsReturnsStocksExpiringOnOrBeforeLimitDate() {
        LocalDate limitDate = LocalDate.of(2026, 5, 31);

        Stock expiredBeforeLimit = new Stock();
        expiredBeforeLimit.setExpirationDate(LocalDate.of(2026, 5, 1));

        Stock expiresOnLimit = new Stock();
        expiresOnLimit.setExpirationDate(limitDate);

        Stock expiresAfterLimit = new Stock();
        expiresAfterLimit.setExpirationDate(LocalDate.of(2026, 6, 1));

        Stock withoutExpiration = new Stock();
        withoutExpiration.setExpirationDate(null);

        when(stockRepository.findAll()).thenReturn(List.of(
                expiredBeforeLimit,
                expiresOnLimit,
                expiresAfterLimit,
                withoutExpiration
        ));

        List<Stock> result = alertService.getExpiringProducts(limitDate);

        assertEquals(2, result.size());
        assertTrue(result.contains(expiredBeforeLimit));
        assertTrue(result.contains(expiresOnLimit));
    }
}
