package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

/**
 * Pruebas unitarias de ProductTransferService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class ProductTransferServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private InventoryMovementRepository movementRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductTransferService productTransferService;

    /**
     * Comprueba el caso de prueba: processTransferMovesAllOriginStockToDestinationAndCreatesTransferMovement.
     */
    @Test
    void processTransferMovesAllOriginStockToDestinationAndCreatesTransferMovement() {
        Location origin = new Location();
        origin.setCode("A1");

        Location destination = new Location();
        destination.setCode("B1");

        Product product1 = new Product();
        product1.setName("Guantes");

        Product product2 = new Product();
        product2.setName("Mascarillas");

        Stock stock1 = new Stock();
        stock1.setProduct(product1);
        stock1.setLocation(origin);
        stock1.setQuantity(4);

        Stock stock2 = new Stock();
        stock2.setProduct(product2);
        stock2.setLocation(origin);
        stock2.setQuantity(6);

        User user = new User();
        user.setUsername("operador");

        TransferRequest request = new TransferRequest();
        request.setOriginCode(" A1 ");
        request.setDestinationCode(" B1 ");

        when(locationRepository.findByCodeIgnoreCase("A1")).thenReturn(Optional.of(origin));
        when(locationRepository.findByCodeIgnoreCase("B1")).thenReturn(Optional.of(destination));
        when(stockRepository.existsByLocationAndQuantityGreaterThan(destination, 0)).thenReturn(false);
        when(stockRepository.findByLocationAndQuantityGreaterThan(origin, 0)).thenReturn(List.of(stock1, stock2));
        when(userRepository.findByUsername("operador")).thenReturn(Optional.of(user));

        productTransferService.processTransfer(request, "operador");

        assertSame(destination, stock1.getLocation());
        assertSame(destination, stock2.getLocation());
        verify(stockRepository).save(stock1);
        verify(stockRepository).save(stock2);

        ArgumentCaptor<InventoryMovement> movementCaptor = ArgumentCaptor.forClass(InventoryMovement.class);
        verify(movementRepository).save(movementCaptor.capture());

        InventoryMovement savedMovement = movementCaptor.getValue();
        assertEquals("TRANSFER", savedMovement.getType());
        assertSame(user, savedMovement.getCreatedBy());
        assertEquals(2, savedMovement.getLines().size());
        assertSame(origin, savedMovement.getLines().get(0).getFromLocation());
        assertSame(destination, savedMovement.getLines().get(0).getToLocation());
        assertEquals(4, savedMovement.getLines().get(0).getQuantity());
        assertSame(origin, savedMovement.getLines().get(1).getFromLocation());
        assertSame(destination, savedMovement.getLines().get(1).getToLocation());
        assertEquals(6, savedMovement.getLines().get(1).getQuantity());
    }

    /**
     * Comprueba el caso de prueba: processTransferThrowsExceptionWhenOriginAndDestinationAreTheSame.
     */
    @Test
    void processTransferThrowsExceptionWhenOriginAndDestinationAreTheSame() {
        TransferRequest request = new TransferRequest();
        request.setOriginCode("A1");
        request.setDestinationCode("a1");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productTransferService.processTransfer(request, "operador"));

        assertEquals("Origen y destino no pueden ser iguales", exception.getMessage());
    }

    /**
     * Comprueba el caso de prueba: processTransferThrowsExceptionWhenDestinationIsOccupied.
     */
    @Test
    void processTransferThrowsExceptionWhenDestinationIsOccupied() {
        Location origin = new Location();
        Location destination = new Location();

        TransferRequest request = new TransferRequest();
        request.setOriginCode("A1");
        request.setDestinationCode("B1");

        when(locationRepository.findByCodeIgnoreCase("A1")).thenReturn(Optional.of(origin));
        when(locationRepository.findByCodeIgnoreCase("B1")).thenReturn(Optional.of(destination));
        when(stockRepository.existsByLocationAndQuantityGreaterThan(destination, 0)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productTransferService.processTransfer(request, "operador"));

        assertEquals("La ubicación destino no está vacía", exception.getMessage());
    }
}
