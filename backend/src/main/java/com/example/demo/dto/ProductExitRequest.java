package com.example.demo.dto;

/**
 * Clase ProductExitRequest del sistema de inventario.
 */
public class ProductExitRequest {

    private String product;
    private Integer quantity;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
