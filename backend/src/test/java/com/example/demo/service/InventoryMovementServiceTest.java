package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.MovementResponse;
import com.example.demo.dto.OperatorResponse;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.InventoryMovementLine;
import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.UserRepository;

/**
 * Pruebas unitarias de InventoryMovementService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class InventoryMovementServiceTest {

    @Mock
    private InventoryMovementRepository movementRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InventoryMovementService inventoryMovementService;

    /**
     * Comprueba el caso de prueba: getRecentMovementsReturnsMovementsOrderedByCreatedAtDescendingAndLimited.
     */
    @Test
    void getRecentMovementsReturnsMovementsOrderedByCreatedAtDescendingAndLimited() {
        InventoryMovement oldMovement = new InventoryMovement();
        oldMovement.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        InventoryMovement newestMovement = new InventoryMovement();
        newestMovement.setCreatedAt(LocalDateTime.of(2026, 1, 3, 10, 0));

        InventoryMovement middleMovement = new InventoryMovement();
        middleMovement.setCreatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));

        when(movementRepository.findAll()).thenReturn(List.of(oldMovement, newestMovement, middleMovement));

        List<InventoryMovement> result = inventoryMovementService.getRecentMovements(2);

        assertIterableEquals(List.of(newestMovement, middleMovement), result);
    }

    /**
     * Comprueba el caso de prueba: getUsersReturnsOnlyActiveUsersMappedToOperatorResponses.
     */
    @Test
    void getUsersReturnsOnlyActiveUsersMappedToOperatorResponses() {
        User activeUser = new User();
        activeUser.setId(1L);
        activeUser.setUsername("activo");
        activeUser.setActive(true);

        User inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setUsername("inactivo");
        inactiveUser.setActive(false);

        User nullActiveUser = new User();
        nullActiveUser.setId(3L);
        nullActiveUser.setUsername("sin_estado");
        nullActiveUser.setActive(null);

        when(userRepository.findAll()).thenReturn(List.of(activeUser, inactiveUser, nullActiveUser));

        List<OperatorResponse> result = inventoryMovementService.getUsers();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("activo", result.get(0).getUsername());
    }

    /**
     * Comprueba el caso de prueba: getMovementResponsesByLocationFiltersLinesByOriginOrDestinationLocation.
     */
    @Test
    void getMovementResponsesByLocationFiltersLinesByOriginOrDestinationLocation() {
        User user = new User();
        user.setUsername("operador");

        Product product = new Product();
        product.setName("Guantes");

        Location matchingOrigin = new Location();
        matchingOrigin.setId(10L);
        matchingOrigin.setName("A1");

        Location otherLocation = new Location();
        otherLocation.setId(20L);
        otherLocation.setName("B1");

        InventoryMovementLine matchingLine = new InventoryMovementLine();
        matchingLine.setProduct(product);
        matchingLine.setFromLocation(matchingOrigin);
        matchingLine.setToLocation(otherLocation);
        matchingLine.setQuantity(3);

        InventoryMovementLine nonMatchingLine = new InventoryMovementLine();
        nonMatchingLine.setProduct(product);
        nonMatchingLine.setFromLocation(otherLocation);
        nonMatchingLine.setToLocation(otherLocation);
        nonMatchingLine.setQuantity(5);

        InventoryMovement movement = new InventoryMovement();
        movement.setId(1L);
        movement.setType("TRANSFER");
        movement.setCreatedBy(user);
        movement.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        movement.setLines(List.of(matchingLine, nonMatchingLine));

        when(movementRepository.findAll()).thenReturn(List.of(movement));

        List<MovementResponse> result = inventoryMovementService.getMovementResponsesByLocation(10L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("TRANSFER", result.get(0).getType());
        assertEquals("Transferencia", result.get(0).getTypeLabel());
        assertEquals("operador", result.get(0).getUsername());
        assertEquals("Guantes", result.get(0).getProductName());
        assertEquals(3, result.get(0).getQuantity());
        assertEquals("A1", result.get(0).getFromLocationName());
        assertEquals("B1", result.get(0).getToLocationName());
    }
}
