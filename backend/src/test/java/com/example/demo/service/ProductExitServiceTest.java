package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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

import com.example.demo.dto.ProductExitRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

/**
 * Pruebas unitarias de ProductExitService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class ProductExitServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private InventoryMovementRepository movementRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductExitService productExitService;

    /**
     * Comprueba el caso de prueba: processExitConsumesStockFromAvailableLotsAndCreatesOutMovement.
     */
    @Test
    void processExitConsumesStockFromAvailableLotsAndCreatesOutMovement() {
        Product product = new Product();
        product.setSku("SKU-1");

        Location location1 = new Location();
        location1.setName("Ubicación 1");

        Location location2 = new Location();
        location2.setName("Ubicación 2");

        Stock oldestLot = new Stock();
        oldestLot.setProduct(product);
        oldestLot.setLocation(location1);
        oldestLot.setQuantity(5);
        oldestLot.setExpirationDate(LocalDate.of(2026, 6, 1));

        Stock secondLot = new Stock();
        secondLot.setProduct(product);
        secondLot.setLocation(location2);
        secondLot.setQuantity(10);
        secondLot.setExpirationDate(LocalDate.of(2026, 7, 1));

        User user = new User();
        user.setUsername("operador");

        ProductExitRequest request = new ProductExitRequest();
        request.setProduct("SKU-1");
        request.setQuantity(8);

        when(productRepository.findFirstBySkuIgnoreCaseOrNameIgnoreCase("SKU-1", "SKU-1"))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername("operador")).thenReturn(Optional.of(user));
        when(stockRepository.findByProductAndQuantityGreaterThanOrderByExpirationDateAscIdAsc(product, 0))
                .thenReturn(List.of(oldestLot, secondLot));

        productExitService.processExit(request, "operador");

        assertEquals(0, oldestLot.getQuantity());
        assertEquals(7, secondLot.getQuantity());
        verify(stockRepository, times(2)).save(any(Stock.class));

        ArgumentCaptor<InventoryMovement> movementCaptor = ArgumentCaptor.forClass(InventoryMovement.class);
        verify(movementRepository).save(movementCaptor.capture());

        InventoryMovement savedMovement = movementCaptor.getValue();
        assertEquals("OUT", savedMovement.getType());
        assertSame(user, savedMovement.getCreatedBy());
        assertEquals(2, savedMovement.getLines().size());
        assertSame(location1, savedMovement.getLines().get(0).getFromLocation());
        assertNull(savedMovement.getLines().get(0).getToLocation());
        assertEquals(5, savedMovement.getLines().get(0).getQuantity());
        assertSame(location2, savedMovement.getLines().get(1).getFromLocation());
        assertEquals(3, savedMovement.getLines().get(1).getQuantity());
    }

    /**
     * Comprueba el caso de prueba: processExitThrowsExceptionWhenThereIsNotEnoughStock.
     */
    @Test
    void processExitThrowsExceptionWhenThereIsNotEnoughStock() {
        Product product = new Product();
        Stock stock = new Stock();
        stock.setQuantity(4);

        User user = new User();

        ProductExitRequest request = new ProductExitRequest();
        request.setProduct("SKU-1");
        request.setQuantity(10);

        when(productRepository.findFirstBySkuIgnoreCaseOrNameIgnoreCase("SKU-1", "SKU-1"))
                .thenReturn(Optional.of(product));
        when(userRepository.findByUsername("operador")).thenReturn(Optional.of(user));
        when(stockRepository.findByProductAndQuantityGreaterThanOrderByExpirationDateAscIdAsc(product, 0))
                .thenReturn(List.of(stock));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productExitService.processExit(request, "operador"));

        assertEquals("Stock insuficiente. Disponible: 4", exception.getMessage());
    }
}
