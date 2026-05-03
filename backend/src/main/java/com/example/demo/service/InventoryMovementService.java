package com.example.demo.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.MovementResponse;
import com.example.demo.dto.OperatorResponse;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.InventoryMovementLine;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.UserRepository;

/**
 * Servicio encargado de consultar, guardar y transformar movimientos de inventario para su visualizacion.
 */
@Service
public class InventoryMovementService {

    private final InventoryMovementRepository movementRepository;
    private final UserRepository userRepository;

    public InventoryMovementService(InventoryMovementRepository movementRepository,
                                    UserRepository userRepository) {
        this.movementRepository = movementRepository;
        this.userRepository = userRepository;
    }

    /**
     * Devuelve todos los movimientos de inventario registrados.
     */
    public List<InventoryMovement> getAllMovements() {
        return movementRepository.findAll();
    }

    /**
     * Busca un movimiento por su identificador.
     */
    public InventoryMovement getMovementById(Long id) {
        return movementRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    /**
     * Guarda un movimiento de inventario.
     */
    public InventoryMovement saveMovement(InventoryMovement movement) {
        return movementRepository.save(movement);
    }

    /**
     * Filtra movimientos por tipo de operacion.
     */
    public List<InventoryMovement> getMovementsByType(String type) {
        return movementRepository.findByType(type);
    }

    /**
     * Filtra movimientos creados por un usuario concreto.
     */
    public List<InventoryMovement> getMovementsByUser(Long userId) {
        return movementRepository.findByCreatedById(userId);
    }

    /**
     * Devuelve los ultimos movimientos registrados, limitando el numero de resultados.
     */
    public List<InventoryMovement> getRecentMovements(int limit) {
        return movementRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(InventoryMovement::getCreatedAt).reversed())
                .limit(limit)
                .toList();
    }

  /**
   * Devuelve los usuarios disponibles para filtrado o visualizacion.
   */
  public List<OperatorResponse> getUsers() {
    return userRepository.findAll()
            .stream()
            .filter(user -> user.getActive() != null && user.getActive())
            .map(user -> new OperatorResponse(user.getId(), user.getUsername()))
            .toList();
}

    /**
     * Devuelve movimientos transformados a DTO filtrados por usuario.
     */
    public List<MovementResponse> getMovementResponsesByUser(Long userId) {
        return movementRepository.findAll()
                .stream()
                .filter(movement -> movement.getCreatedBy() != null &&
                        movement.getCreatedBy().getId().equals(userId))
                .sorted(Comparator.comparing(InventoryMovement::getCreatedAt).reversed())
                .flatMap(movement -> movement.getLines()
                        .stream()
                        .map(line -> mapToResponse(movement, line)))
                .toList();
    }

    /**
     * Devuelve movimientos transformados a DTO filtrados por ubicacion.
     */
    public List<MovementResponse> getMovementResponsesByLocation(Long locationId) {
        return movementRepository.findAll()
                .stream()
                .filter(movement -> movement.getLines()
                        .stream()
                        .anyMatch(line ->
                                (line.getFromLocation() != null &&
                                        line.getFromLocation().getId().equals(locationId)) ||
                                (line.getToLocation() != null &&
                                        line.getToLocation().getId().equals(locationId))
                        ))
                .sorted(Comparator.comparing(InventoryMovement::getCreatedAt).reversed())
                .flatMap(movement -> movement.getLines()
                        .stream()
                        .filter(line ->
                                (line.getFromLocation() != null &&
                                        line.getFromLocation().getId().equals(locationId)) ||
                                (line.getToLocation() != null &&
                                        line.getToLocation().getId().equals(locationId))
                        )
                        .map(line -> mapToResponse(movement, line)))
                .toList();
    }

    /**
     * Convierte una entidad de movimiento y su linea asociada en un DTO de respuesta.
     */
    private MovementResponse mapToResponse(InventoryMovement movement, InventoryMovementLine line) {
        User user = movement.getCreatedBy();

        return new MovementResponse(
                movement.getId(),
                movement.getType(),
                getTypeLabel(movement.getType()),
                user != null ? user.getUsername() : "-",
                line.getProduct() != null ? line.getProduct().getName() : "-",
                line.getQuantity(),
                line.getFromLocation() != null ? line.getFromLocation().getName() : null,
                line.getToLocation() != null ? line.getToLocation().getName() : null,
                movement.getCreatedAt()
        );
    }

    /**
     * Convierte el codigo interno del tipo de movimiento en una etiqueta legible.
     */
    private String getTypeLabel(String type) {
        if (type == null) return "-";

        return switch (type.toUpperCase()) {
            case "IN" -> "Entrada";
            case "OUT" -> "Salida";
            case "TRANSFER" -> "Transferencia";
            case "ADJUST" -> "Ajuste";
            default -> type;
        };
    }
}
