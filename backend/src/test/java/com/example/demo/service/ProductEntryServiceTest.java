package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.ProductEntryRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

/**
 * Pruebas unitarias de ProductEntryService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class ProductEntryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private InventoryMovementRepository movementRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductEntryService productEntryService;

    /**
     * Comprueba el caso de prueba: processEntryCreatesStockAndInMovementInBestEmptyLocation.
     */
    @Test
    void processEntryCreatesStockAndInMovementInBestEmptyLocation() {
        Product product = new Product();
        product.setSku("SKU-1");
        product.setName("Guantes");
        product.setCriticality("ALTA");

        Location occupiedHighLocation = new Location();
        occupiedHighLocation.setId(1L);
        occupiedHighLocation.setCode("A1");

        Location emptyHighLocation = new Location();
        emptyHighLocation.setId(2L);
        emptyHighLocation.setCode("A2");

        User user = new User();
        user.setUsername("operador");

        ProductEntryRequest request = new ProductEntryRequest();
        request.setProduct(" SKU-1 ");
        request.setQuantity(12);
        request.setLotCode(" L-001 ");
        request.setExpirationDate(LocalDate.of(2027, 1, 31));

        when(productRepository.findFirstBySkuIgnoreCaseOrNameIgnoreCase("SKU-1", "SKU-1"))
                .thenReturn(Optional.of(product));
        when(locationRepository.findByCriticalityIgnoreCase("ALTA"))
                .thenReturn(List.of(occupiedHighLocation, emptyHighLocation));
        when(stockRepository.existsByLocationAndQuantityGreaterThan(occupiedHighLocation, 0)).thenReturn(true);
        when(stockRepository.existsByLocationAndQuantityGreaterThan(emptyHighLocation, 0)).thenReturn(false);
        when(userRepository.findByUsername("operador")).thenReturn(Optional.of(user));

        productEntryService.processEntry(request, "operador");

        ArgumentCaptor<Stock> stockCaptor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(stockCaptor.capture());

        Stock savedStock = stockCaptor.getValue();
        assertSame(product, savedStock.getProduct());
        assertSame(emptyHighLocation, savedStock.getLocation());
        assertEquals(12, savedStock.getQuantity());
        assertEquals("L-001", savedStock.getLotCode());
        assertEquals(LocalDate.of(2027, 1, 31), savedStock.getExpirationDate());

        ArgumentCaptor<InventoryMovement> movementCaptor = ArgumentCaptor.forClass(InventoryMovement.class);
        verify(movementRepository).save(movementCaptor.capture());

        InventoryMovement savedMovement = movementCaptor.getValue();
        assertEquals("IN", savedMovement.getType());
        assertSame(user, savedMovement.getCreatedBy());
        assertEquals(1, savedMovement.getLines().size());
        assertSame(product, savedMovement.getLines().get(0).getProduct());
        assertSame(emptyHighLocation, savedMovement.getLines().get(0).getToLocation());
        assertEquals(12, savedMovement.getLines().get(0).getQuantity());
    }

    /**
     * Comprueba el caso de prueba: processEntryThrowsExceptionWhenQuantityIsInvalid.
     */
    @Test
    void processEntryThrowsExceptionWhenQuantityIsInvalid() {
        ProductEntryRequest request = new ProductEntryRequest();
        request.setProduct("SKU-1");
        request.setQuantity(0);
        request.setLotCode("L-001");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productEntryService.processEntry(request, "operador"));

        assertEquals("La cantidad debe ser mayor que 0", exception.getMessage());
    }
}
