package com.example.demo.repository;

import com.example.demo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findByCode(String code);

    List<Location> findByNameContainingIgnoreCase(String name);

}