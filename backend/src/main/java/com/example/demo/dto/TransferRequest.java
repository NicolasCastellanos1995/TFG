package com.example.demo.dto;

/**
 * Clase TransferRequest del sistema de inventario.
 */
public class TransferRequest {

    private String originCode;
    private String destinationCode;

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }
}
