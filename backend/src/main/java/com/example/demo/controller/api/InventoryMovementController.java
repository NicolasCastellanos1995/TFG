package com.example.demo.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MovementResponse;
import com.example.demo.dto.OperatorResponse;
import com.example.demo.dto.ProductEntryRequest;
import com.example.demo.dto.ProductExitRequest;
import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.service.InventoryMovementService;
import com.example.demo.service.ProductEntryService;
import com.example.demo.service.ProductExitService;
import com.example.demo.service.ProductTransferService;

/**
 * Controlador REST encargado de consultar los movimientos de inventario registrados en el sistema.
 */
@RestController
@RequestMapping("/api/movements")
public class InventoryMovementController {

    private final InventoryMovementService movementService;
    private final ProductEntryService productEntryService;
    private final ProductExitService productExitService;
    private final ProductTransferService productTransferService;

    public InventoryMovementController(
            InventoryMovementService movementService,
            ProductEntryService productEntryService,
            ProductExitService productExitService,
            ProductTransferService productTransferService
    ) {
        this.movementService = movementService;
        this.productEntryService = productEntryService;
        this.productExitService = productExitService;
        this.productTransferService = productTransferService;
    }
@GetMapping("/users")
/**
 * Devuelve los usuarios disponibles para filtrado o visualizacion.
 */
public List<OperatorResponse> getUsers() {
    return movementService.getUsers();
}
@GetMapping("/by-operator/{operatorId}")
public List<MovementResponse> getMovementsByOperator(@PathVariable Long operatorId) {
    return movementService.getMovementResponsesByUser(operatorId);
}

@GetMapping("/by-location/{locationId}")
public List<MovementResponse> getMovementsByLocation(@PathVariable Long locationId) {
    return movementService.getMovementResponsesByLocation(locationId);
}

    @PostMapping("/entries")
    public ResponseEntity<Map<String, String>> createEntry(
            @RequestBody ProductEntryRequest request,
            Authentication authentication
    ) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "No autenticado"));
            }

            productEntryService.processEntry(request, authentication.getName());

            return ResponseEntity.ok(
                    Map.of("message", "Entrada registrada correctamente")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/exits")
    public ResponseEntity<Map<String, String>> createExit(
            @RequestBody ProductExitRequest request,
            Authentication authentication
    ) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "No autenticado"));
            }

            productExitService.processExit(request, authentication.getName());

            return ResponseEntity.ok(
                    Map.of("message", "Salida registrada correctamente")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/transfers")
    public ResponseEntity<Map<String, String>> createTransfer(
            @RequestBody TransferRequest request,
            Authentication authentication
    ) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "No autenticado"));
            }

            productTransferService.processTransfer(request, authentication.getName());

            return ResponseEntity.ok(
                    Map.of("message", "Transferencia registrada correctamente")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    /**
     * Devuelve todos los movimientos de inventario registrados.
     */
    public List<InventoryMovement> getAllMovements() {
        return movementService.getAllMovements();
    }

}
