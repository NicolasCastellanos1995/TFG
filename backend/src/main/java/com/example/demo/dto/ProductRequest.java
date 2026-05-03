package com.example.demo.dto;

/**
 * Clase ProductRequest del sistema de inventario.
 */
public class ProductRequest {

    private String sku;
    private String name;
    private Long categoryId;
    private String unit;
    private Integer minStock;
    /** Criticidad del producto utilizada para priorizar su ubicacion en almacen. */
    private String criticality;

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Integer getMinStock() { return minStock; }
    public void setMinStock(Integer minStock) { this.minStock = minStock; }

    /**
     * Devuelve la criticidad asignada a la ubicacion.
     */
    public String getCriticality() { return criticality; }
    public void setCriticality(String criticality) { this.criticality = criticality; }
}
