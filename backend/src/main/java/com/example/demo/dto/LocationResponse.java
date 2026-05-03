package com.example.demo.dto;

/**
 * Clase LocationResponse del sistema de inventario.
 */
public class LocationResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String criticality;
    private Double coordX;
    private Double coordY;
    private Double coordZ;

    public LocationResponse(
            Long id,
            String code,
            String name,
            String description,
            String criticality,
            Double coordX,
            Double coordY,
            Double coordZ
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.criticality = criticality;
        this.coordX = coordX;
        this.coordY = coordY;
        this.coordZ = coordZ;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCriticality() { return criticality; }
    public Double getCoordX() { return coordX; }
    public Double getCoordY() { return coordY; }
    public Double getCoordZ() { return coordZ; }
}
