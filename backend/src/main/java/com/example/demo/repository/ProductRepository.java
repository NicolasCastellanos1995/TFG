package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Product;

/**
 * Clase ProductRepository del sistema de inventario.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);

    Optional<Product> findFirstBySkuIgnoreCaseOrNameIgnoreCase(String sku, String name);


    //como no tengo una conexion automatica me creo una query manual
    @Query("SELECT DISTINCT s.product FROM Stock s WHERE s.location.id = :locationId")
    List<Product> findProductsByLocationId(Long locationId);
}
