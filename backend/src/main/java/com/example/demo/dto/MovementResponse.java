package com.example.demo.dto;

import java.time.LocalDateTime;

/**
 * Clase MovementResponse del sistema de inventario.
 */
public class MovementResponse {

    private Long id;
    private String type;
    private String typeLabel;
    private String username;
    private String productName;
    private Integer quantity;
    private String fromLocationName;
    private String toLocationName;
    private LocalDateTime createdAt;

    public MovementResponse(Long id, String type, String typeLabel, String username,
                            String productName, Integer quantity,
                            String fromLocationName, String toLocationName,
                            LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.typeLabel = typeLabel;
        this.username = username;
        this.productName = productName;
        this.quantity = quantity;
        this.fromLocationName = fromLocationName;
        this.toLocationName = toLocationName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    /**
     * Convierte el codigo interno del tipo de movimiento en una etiqueta legible.
     */
    public String getTypeLabel() { return typeLabel; }
    public String getUsername() { return username; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public String getFromLocationName() { return fromLocationName; }
    public String getToLocationName() { return toLocationName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
