package com.example.demo.service;

import com.example.demo.entity.Stock;
import com.example.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public List<Stock> getStockByProduct(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    public List<Stock> getStockByLocation(Long locationId) {
        return stockRepository.findByLocationId(locationId);
    }

    public Integer getTotalStockByProduct(Long productId) {
        List<Stock> stockList = stockRepository.findByProductId(productId);

        int total = 0;
        for (Stock stock : stockList) {
            total += stock.getQuantity();
        }

        return total;
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
}