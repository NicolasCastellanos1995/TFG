package com.example.demo.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LocationResponse;
import com.example.demo.entity.Location;
import com.example.demo.repository.LocationRepository;

/**
 * Controlador REST para consultar ubicaciones del almacen.
 */
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getLocations() {
        List<LocationResponse> locations = locationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/empty")
    public ResponseEntity<List<LocationResponse>> getEmptyLocations() {
        List<LocationResponse> locations = locationRepository.findEmptyLocations()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/occupied")
    public ResponseEntity<List<LocationResponse>> getOccupiedLocations() {
        List<LocationResponse> locations = locationRepository.findOccupiedLocations()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(locations);
    }

    @PostMapping
    public ResponseEntity<Void> createLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLocation(
            @PathVariable Long id,
            @RequestBody Location request
    ) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        location.setCode(request.getCode());
        location.setName(request.getName());
        location.setDescription(request.getDescription());
        location.setCriticality(request.getCriticality());
        location.setCoordX(request.getCoordX());
        location.setCoordY(request.getCoordY());
        location.setCoordZ(request.getCoordZ());

        locationRepository.save(location);

        return ResponseEntity.ok().build();
    }

    /**
     * Convierte una entidad de movimiento y su linea asociada en un DTO de respuesta.
     */
    private LocationResponse mapToResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getCode(),
                location.getName(),
                location.getDescription(),
                location.getCriticality(),
                location.getCoordX(),
                location.getCoordY(),
                location.getCoordZ()
        );
    }
}
