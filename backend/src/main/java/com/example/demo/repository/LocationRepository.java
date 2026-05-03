package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Location;

/**
 * Clase LocationRepository del sistema de inventario.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCode(String code);
    Optional<Location> findByCodeIgnoreCase(String code);
        @Query("""
        SELECT l FROM Location l
        WHERE NOT EXISTS (
            SELECT s FROM Stock s
            WHERE s.location = l
            AND s.quantity > 0
        )
    """)
    List<Location> findEmptyLocations();

    @Query("""
        SELECT DISTINCT l FROM Location l
        JOIN Stock s ON s.location = l
        WHERE s.quantity > 0
    """)
    List<Location> findOccupiedLocations();

    List<Location> findByNameContainingIgnoreCase(String name);

   List<Location> findByCriticalityIgnoreCase(String criticality);
}
