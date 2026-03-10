package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "location")
    private List<Stock> stocks = new ArrayList<>();

    @OneToMany(mappedBy = "fromLocation")
    private List<InventoryMovementLine> outgoingLines = new ArrayList<>();

    @OneToMany(mappedBy = "toLocation")
    private List<InventoryMovementLine> incomingLines = new ArrayList<>();

    public Location() {
    }

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
}