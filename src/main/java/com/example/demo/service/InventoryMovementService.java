package com.example.demo.service;

import com.example.demo.entity.InventoryMovement;
import com.example.demo.repository.InventoryMovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryMovementService {

    private final InventoryMovementRepository movementRepository;

    public InventoryMovementService(InventoryMovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    /**
     * Devuelve todos los movimientos de inventario registrados
     */
    public List<InventoryMovement> getAllMovements() {
        return movementRepository.findAll();
    }

    /**
     * Busca un movimiento por su ID
     */
    public InventoryMovement getMovementById(Long id) {
        return movementRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    /**
     * Guarda un movimiento de inventario
     * Puede ser una entrada, salida o transferencia
     */
    public InventoryMovement saveMovement(InventoryMovement movement) {
        return movementRepository.save(movement);
    }

    /**
     * Devuelve todos los movimientos de un tipo concreto
     * Ejemplo:
     * ENTRY
     * EXIT
     * TRANSFER
     */
    public List<InventoryMovement> getMovementsByType(String type) {
        return movementRepository.findByType(type);
    }

    /**
     * Devuelve todos los movimientos creados por un usuario
     */
    public List<InventoryMovement> getMovementsByUser(Long userId) {
        return movementRepository.findByCreatedById(userId);
    }
}