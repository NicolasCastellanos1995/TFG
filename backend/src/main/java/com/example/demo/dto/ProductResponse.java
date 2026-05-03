package com.example.demo.dto;

/**
 * Clase ProductResponse del sistema de inventario.
 */
public class ProductResponse {

    private Long id;
    private String sku;
    private String name;
    private String unit;
    private Integer minStock;
    private String criticality;
    private Long categoryId;
    private String categoryName;

    public ProductResponse(Long id, String sku, String name, String unit,
                           Integer minStock, String criticality,
                           Long categoryId, String categoryName) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.unit = unit;
        this.minStock = minStock;
        this.criticality = criticality;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getUnit() { return unit; }
    public Integer getMinStock() { return minStock; }
    public String getCriticality() { return criticality; }
    public Long getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
}
