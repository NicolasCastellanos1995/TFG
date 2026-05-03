package com.example.demo.controller.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.AlertService;
import com.example.demo.service.InventoryMovementService;
import com.example.demo.service.ProductService;
import com.example.demo.service.StockService;

/**
 * Controlador web responsable de cargar la informacion principal del panel de inventario.
 */
@Controller
public class DashboardController {

    private final ProductService productService;
    private final StockService stockService;
    private final InventoryMovementService movementService;
    private final AlertService alertService;

    public DashboardController(ProductService productService,
                               StockService stockService,
                               InventoryMovementService movementService,
                               AlertService alertService) {
        this.productService = productService;
        this.stockService = stockService;
        this.movementService = movementService;
        this.alertService = alertService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalStock", stockService.getAllStock().size());

        model.addAttribute("lowStockAlerts", alertService.getLowStockProducts());

        // Productos que vencen en 7 días o menos
        model.addAttribute("expiringAlerts", alertService.getExpiringProducts(LocalDate.now().plusDays(7)));

     model.addAttribute("movements", movementService.getRecentMovements(10));
        return "dashboard";
    }
}
