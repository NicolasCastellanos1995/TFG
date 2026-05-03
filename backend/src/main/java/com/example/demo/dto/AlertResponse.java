package com.example.demo.dto;

import java.time.LocalDate;

/**
 * Clase AlertResponse del sistema de inventario.
 */
public class AlertResponse {
    private String productSku;
    private String productName;
    private String locationCode;
    private String locationName;
    private Integer quantity;
    private Integer minStock;
    private String lotCode;
    private LocalDate expirationDate;

    public AlertResponse(String productSku, String productName,
                         String locationCode, String locationName,
                         Integer quantity, Integer minStock,
                         String lotCode, LocalDate expirationDate) {
        this.productSku = productSku;
        this.productName = productName;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.quantity = quantity;
        this.minStock = minStock;
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
    }

    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getLocationCode() { return locationCode; }
    public String getLocationName() { return locationName; }
    public Integer getQuantity() { return quantity; }
    public Integer getMinStock() { return minStock; }
    public String getLotCode() { return lotCode; }
    public LocalDate getExpirationDate() { return expirationDate; }
}
