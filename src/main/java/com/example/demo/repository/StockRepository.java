package com.example.demo.repository;

import com.example.demo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByProductId(Long productId);
    List<Stock> findByLocationId(Long locationId);
    java.util.Optional<Stock> findByProductIdAndLocationId(Long productId, Long locationId);

}