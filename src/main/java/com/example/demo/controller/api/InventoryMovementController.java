package com.example.demo.controller.api;

import com.example.demo.entity.InventoryMovement;
import com.example.demo.service.InventoryMovementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movements")
public class InventoryMovementController {

    // Service de movimientos
    private final InventoryMovementService movementService;

    // Inyección por constructor
    public InventoryMovementController(InventoryMovementService movementService) {
        this.movementService = movementService;
    }

    /**
     * GET /movements
     * Devuelve todos los movimientos
     */
    @GetMapping
    public List<InventoryMovement> getAllMovements() {
        return movementService.getAllMovements();
    }

    /**
     * GET /movements/{id}
     * Devuelve un movimiento por id
     */
    @GetMapping("/{id}")
    public InventoryMovement getMovementById(@PathVariable Long id) {
        return movementService.getMovementById(id);
    }

    /**
     * GET /movements/type/{type}
     * Devuelve movimientos por tipo
     * Ejemplo: ENTRY, EXIT, TRANSFER
     */
    @GetMapping("/type/{type}")
    public List<InventoryMovement> getMovementsByType(@PathVariable String type) {
        return movementService.getMovementsByType(type);
    }

    /**
     * GET /movements/user/{userId}
     * Devuelve movimientos creados por un usuario concreto
     */
    @GetMapping("/user/{userId}")
    public List<InventoryMovement> getMovementsByUser(@PathVariable Long userId) {
        return movementService.getMovementsByUser(userId);
    }

    /**
     * POST /movements
     * Guarda un nuevo movimiento
     */
    @PostMapping
    public InventoryMovement createMovement(@RequestBody InventoryMovement movement) {
        return movementService.saveMovement(movement);
    }
}