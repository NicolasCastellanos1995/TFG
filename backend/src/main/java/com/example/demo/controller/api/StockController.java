package com.example.demo.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.StockResponse;
import com.example.demo.entity.Stock;
import com.example.demo.service.StockService;

/**
 * Controlador REST encargado de consultar el stock disponible por producto, ubicacion o filtros de busqueda.
 */
@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    /**
     * Devuelve todos los registros de stock existentes.
     */
    public List<Stock> getAllStock() {
        return stockService.getAllStock();
    }

    @GetMapping("/product/{productId}")
    /**
     * Devuelve el stock asociado a un producto concreto.
     */
    public List<Stock> getStockByProduct(@PathVariable Long productId) {
        return stockService.getStockByProduct(productId);
    }

    @GetMapping("/location/{locationId}")
    /**
     * Devuelve el stock almacenado en una ubicacion concreta.
     */
    public List<Stock> getStockByLocation(@PathVariable Long locationId) {
        return stockService.getStockByLocation(locationId);
    }

    @GetMapping("/product/{productId}/total")
    /**
     * Calcula la cantidad total disponible de un producto sumando todos sus lotes y ubicaciones.
     */
    public Integer getTotalStockByProduct(@PathVariable Long productId) {
        return stockService.getTotalStockByProduct(productId);
    }

    @PostMapping
    /**
     * Guarda un registro de stock.
     */
    public Stock saveStock(@RequestBody Stock stock) {
        return stockService.saveStock(stock);
    }

    @GetMapping("/search")
    public List<StockResponse> searchStock(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String product
    ) {
        List<Stock> stocks;

        if (location != null && !location.trim().isEmpty()) {
            stocks = stockService.getStockByLocationCodeOrName(location.trim());
        } else if (product != null && !product.trim().isEmpty()) {
            stocks = stockService.getStockByProductSkuOrName(product.trim());
        } else {
            stocks = stockService.getAllStock();
        }

        return stocks.stream()
                .filter(stock -> stock.getQuantity() != null && stock.getQuantity() > 0)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Convierte una entidad de movimiento y su linea asociada en un DTO de respuesta.
     */
    private StockResponse mapToResponse(Stock stock) {
        return new StockResponse(
                stock.getProduct().getSku(),
                stock.getProduct().getName(),
                stock.getLocation().getCode(),
                stock.getLocation().getName(),
                stock.getQuantity()
        );
    }
}
