package com.example.demo.dto;

/**
 * Clase OperatorResponse del sistema de inventario.
 */
public class OperatorResponse {

    private Long id;
    private String username;

    public OperatorResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
}
