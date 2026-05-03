package com.example.demo.dto;

/**
 * Clase StockResponse del sistema de inventario.
 */
public class StockResponse {

    private String productSku;
    private String productName;
    private String locationCode;
    private String locationName;
    private Integer quantity;

    public StockResponse(
            String productSku,
            String productName,
            String locationCode,
            String locationName,
            Integer quantity
    ) {
        this.productSku = productSku;
        this.productName = productName;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.quantity = quantity;
    }

    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getLocationCode() { return locationCode; }
    public String getLocationName() { return locationName; }
    public Integer getQuantity() { return quantity; }
}
