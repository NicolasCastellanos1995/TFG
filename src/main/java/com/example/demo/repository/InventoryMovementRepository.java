package com.example.demo.repository;

import com.example.demo.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Integer> {
    List<InventoryMovement> findByType(String type);
    List<InventoryMovement> findByCreatedById(Long createdById);

}