package com.example.demo.repository;

import com.example.demo.entity.InventoryMovementLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementLineRepository extends JpaRepository<InventoryMovementLine, Integer> {

    List<InventoryMovementLine> findByMovementId(Long movementId);
    List<InventoryMovementLine> findByProductId(Long productId);
    List<InventoryMovementLine> findByFromLocationId(Long fromLocationId);
    List<InventoryMovementLine> findByToLocationId(Long toLocationId);

}