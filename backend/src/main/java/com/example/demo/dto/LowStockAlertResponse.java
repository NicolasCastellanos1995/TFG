package com.example.demo.dto;

/**
 * Clase LowStockAlertResponse del sistema de inventario.
 */
public class LowStockAlertResponse {

    private String productSku;
    private String productName;
    private Integer quantity;
    private Integer minStock;

    public LowStockAlertResponse(String productSku, String productName, Integer quantity, Integer minStock) {
        this.productSku = productSku;
        this.productName = productName;
        this.quantity = quantity;
        this.minStock = minStock;
    }

    public String getProductSku() {
        return productSku;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getMinStock() {
        return minStock;
    }
}
