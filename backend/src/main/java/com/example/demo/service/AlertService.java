package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LowStockAlertResponse;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;

/**
 * Servicio responsable de detectar situaciones relevantes de inventario, como bajo stock, caducidades proximas y productos caducados.
 */
@Service
public class AlertService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public AlertService(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    /**
     * Devuelve los productos cuyo stock total esta por debajo del minimo configurado.
     */
    public List<LowStockAlertResponse> getLowStockProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<Stock> allStock = stockRepository.findAll();

        List<LowStockAlertResponse> lowStockList = new ArrayList<>();

        for (Product product : allProducts) {
            int totalQuantity = allStock.stream()
                    .filter(stock -> stock.getProduct().getId().equals(product.getId()))
                    .mapToInt(stock -> stock.getQuantity() == null ? 0 : stock.getQuantity())
                    .sum();

            int minStock = product.getMinStock() == null ? 0 : product.getMinStock();

            if (totalQuantity <= minStock) {
                lowStockList.add(new LowStockAlertResponse(
                        product.getSku(),
                        product.getName(),
                        totalQuantity,
                        minStock
                ));
            }
        }

        return lowStockList;
    }

    /**
     * Devuelve los lotes que caducan antes de la fecha limite indicada.
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
     * Devuelve los lotes cuya fecha de caducidad ya ha vencido.
     */
    public List<Stock> getExpiredProducts() {
        return getExpiringProducts(LocalDate.now());
    }
}
