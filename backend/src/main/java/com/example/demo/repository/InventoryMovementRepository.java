package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.InventoryMovement;

/**
 * Clase InventoryMovementRepository del sistema de inventario.
 */
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Integer> {

    List<InventoryMovement> findByType(String type);

    List<InventoryMovement> findByCreatedById(Long createdById);

    @EntityGraph(attributePaths = {
            "createdBy",
            "lines",
            "lines.product",
            "lines.fromLocation",
            "lines.toLocation"
    })
    List<InventoryMovement> findAll();
}
