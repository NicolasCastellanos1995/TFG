package com.example.demo.controller.api;

import com.example.demo.entity.Stock;
import com.example.demo.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getAllStock() {
        return stockService.getAllStock();
    }

    @GetMapping("/product/{productId}")
    public List<Stock> getStockByProduct(@PathVariable Long productId) {
        return stockService.getStockByProduct(productId);
    }

    @GetMapping("/location/{locationId}")
    public List<Stock> getStockByLocation(@PathVariable Long locationId) {
        return stockService.getStockByLocation(locationId);
    }

    @GetMapping("/product/{productId}/total")
    public Integer getTotalStockByProduct(@PathVariable Long productId) {
        return stockService.getTotalStockByProduct(productId);
    }

    @PostMapping
    public Stock saveStock(@RequestBody Stock stock) {
        return stockService.saveStock(stock);
    }
}