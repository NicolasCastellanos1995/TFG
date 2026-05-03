package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una ubicacion fisica del almacen.
 */
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 255)
    private String description;

    /** Criticidad asociada a la ubicacion para ordenar la asignacion de productos. */
    @Column(nullable = false, length = 20)
    private String criticality;

    @Column(name = "coord_x")
    private Double coordX;

    @Column(name = "coord_y")
    private Double coordY;

    @Column(name = "coord_z")
    private Double coordZ;
    @OneToMany(mappedBy = "location")
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "fromLocation")
    private List<InventoryMovementLine> outgoingLines = new ArrayList<>();

    @OneToMany(mappedBy = "toLocation")
    private List<InventoryMovementLine> incomingLines = new ArrayList<>();

    public Location() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Devuelve la criticidad asignada a la ubicacion.
     */
    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<InventoryMovementLine> getOutgoingLines() {
        return outgoingLines;
    }

    public void setOutgoingLines(List<InventoryMovementLine> outgoingLines) {
        this.outgoingLines = outgoingLines;
    }

    public List<InventoryMovementLine> getIncomingLines() {
        return incomingLines;
    }

    public void setIncomingLines(List<InventoryMovementLine> incomingLines) {
        this.incomingLines = incomingLines;
    }
    public Double getCoordX() {
    return coordX;
}

public void setCoordX(Double coordX) {
    this.coordX = coordX;
}

public Double getCoordY() {
    return coordY;
}

public void setCoordY(Double coordY) {
    this.coordY = coordY;
}

public Double getCoordZ() {
    return coordZ;
}

public void setCoordZ(Double coordZ) {
    this.coordZ = coordZ;
}
}
