package com.example.demo.service;

import com.example.demo.entity.Stock;
import com.example.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {

    private final StockRepository stockRepository;

    public AlertService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Devuelve los registros de stock cuyo quantity
     * es menor o igual al stock mínimo del producto
     */
    public List<Stock> getLowStockProducts() {
        List<Stock> allStock = stockRepository.findAll();
        List<Stock> lowStockList = new ArrayList<>();

        for (Stock stock : allStock) {
            if (stock.getQuantity() <= stock.getProduct().getMinStock()) {
                lowStockList.add(stock);
            }
        }

        return lowStockList;
    }

    /**
     * Devuelve productos que caducan antes o en la fecha indicada
     */
    public List<Stock> getExpiringProducts(LocalDate limitDate) {
        List<Stock> allStock = stockRepository.findAll();
        List<Stock> expiringList = new ArrayList<>();

        for (Stock stock : allStock) {
            if (stock.getExpirationDate() != null &&
                    !stock.getExpirationDate().isAfter(limitDate)) {
                expiringList.add(stock);
            }
        }

        return expiringList;
    }

    /**
     * Devuelve productos ya caducados
     */
    public List<Stock> getExpiredProducts() {
        return getExpiringProducts(LocalDate.now());
    }
}