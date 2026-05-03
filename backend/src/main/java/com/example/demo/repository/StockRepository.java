package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;

/**
 * Clase StockRepository del sistema de inventario.
 */
public interface StockRepository extends JpaRepository<Stock, Long> {

    boolean existsByLocation(Location location);

    boolean existsByLocationAndQuantityGreaterThan(Location location, Integer quantity);

    List<Stock> findByProduct(Product product);

    List<Stock> findByLocation(Location location);

    List<Stock> findByProductAndQuantityGreaterThanOrderByExpirationDateAscIdAsc(
            Product product,
            Integer quantity
    );

    List<Stock> findByLocationAndQuantityGreaterThan(Location location, Integer quantity);

    @Query("""
        SELECT s FROM Stock s
        WHERE s.quantity > 0
        AND (
            LOWER(s.location.code) = LOWER(:location)
            OR LOWER(s.location.name) = LOWER(:location)
        )
    """)
    List<Stock> findByLocationCodeOrName(String location);

    @Query("""
        SELECT s FROM Stock s
        WHERE s.quantity > 0
        AND (
            LOWER(s.product.sku) = LOWER(:product)
            OR LOWER(s.product.name) = LOWER(:product)
        )
    """)
    List<Stock> findByProductSkuOrName(String product);
}
