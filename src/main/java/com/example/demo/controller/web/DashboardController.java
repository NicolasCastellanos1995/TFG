package com.example.demo.controller.web;

import com.example.demo.service.AlertService;
import com.example.demo.service.InventoryMovementService;
import com.example.demo.service.ProductService;
import com.example.demo.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("expiringAlerts", alertService.getExpiredProducts());
        model.addAttribute("movements", movementService.getAllMovements());

        return "dashboard";
    }
}