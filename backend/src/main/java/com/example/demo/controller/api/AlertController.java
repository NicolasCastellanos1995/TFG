package com.example.demo.controller.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AlertResponse;
import com.example.demo.dto.LowStockAlertResponse;
import com.example.demo.entity.Stock;
import com.example.demo.service.AlertService;

/**
 * Controlador REST encargado de exponer las alertas de inventario, como bajo stock, productos caducados o proximos a caducar.
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/low-stock")
    public List<LowStockAlertResponse> getLowStock() {
        return alertService.getLowStockProducts();
    }

    @GetMapping("/expiring")
    /**
     * Devuelve los lotes que caducan antes de la fecha limite indicada.
     */
    public List<AlertResponse> getExpiringProducts(
            @RequestParam(defaultValue = "7") int days
    ) {
        LocalDate limitDate = LocalDate.now().plusDays(days);

        return alertService.getExpiringProducts(limitDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Convierte una entidad de movimiento y su linea asociada en un DTO de respuesta.
     */
    private AlertResponse mapToResponse(Stock stock) {
        return new AlertResponse(
                stock.getProduct().getSku(),
                stock.getProduct().getName(),
                stock.getLocation() != null ? stock.getLocation().getCode() : null,
                stock.getLocation() != null ? stock.getLocation().getName() : null,
                stock.getQuantity(),
                stock.getProduct().getMinStock(),
                stock.getLotCode(),
                stock.getExpirationDate()
        );
    }
}
