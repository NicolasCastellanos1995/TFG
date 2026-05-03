package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;

/**
 * Servicio encargado de consultar y guardar informacion de stock.
 */
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    public StockService(
            StockRepository stockRepository,
            ProductRepository productRepository,
            LocationRepository locationRepository
    ) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Devuelve todos los registros de stock existentes.
     */
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    /**
     * Busca stock asociado a una ubicacion por codigo o nombre.
     */
    public List<Stock> getStockByLocationCodeOrName(String location) {
        return stockRepository.findByLocationCodeOrName(location);
    }

    /**
     * Busca stock asociado a un producto por SKU o nombre.
     */
    public List<Stock> getStockByProductSkuOrName(String product) {
        return stockRepository.findByProductSkuOrName(product);
    }

    /**
     * Devuelve el stock asociado a un producto concreto.
     */
    public List<Stock> getStockByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return stockRepository.findByProduct(product);
    }

    /**
     * Devuelve el stock almacenado en una ubicacion concreta.
     */
    public List<Stock> getStockByLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        return stockRepository.findByLocation(location);
    }

    /**
     * Calcula la cantidad total disponible de un producto sumando todos sus lotes y ubicaciones.
     */
    public Integer getTotalStockByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        List<Stock> stockList = stockRepository.findByProduct(product);

        return stockList.stream()
                .mapToInt(stock -> stock.getQuantity() != null ? stock.getQuantity() : 0)
                .sum();
    }

    /**
     * Guarda un registro de stock.
     */
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
}
