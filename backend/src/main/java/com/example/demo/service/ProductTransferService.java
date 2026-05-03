package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.InventoryMovementLine;
import com.example.demo.entity.Location;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

/**
 * Servicio que procesa transferencias de stock entre ubicaciones del almacen.
 */
@Service
public class ProductTransferService {

    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final InventoryMovementRepository movementRepository;
    private final UserRepository userRepository;

    public ProductTransferService(
            LocationRepository locationRepository,
            StockRepository stockRepository,
            InventoryMovementRepository movementRepository,
            UserRepository userRepository
    ) {
        this.locationRepository = locationRepository;
        this.stockRepository = stockRepository;
        this.movementRepository = movementRepository;
        this.userRepository = userRepository;
    }

    /**
     * Procesa una transferencia de stock entre dos ubicaciones y registra el movimiento.
     */
    @Transactional
    public void processTransfer(TransferRequest request, String username) {

        // Valida que existan ubicaciones de origen y destino.
        if (request.getOriginCode() == null || request.getOriginCode().trim().isEmpty()) {
            throw new RuntimeException("Debe indicar la ubicación origen");
        }

        if (request.getDestinationCode() == null || request.getDestinationCode().trim().isEmpty()) {
            throw new RuntimeException("Debe indicar la ubicación destino");
        }

        String originCode = request.getOriginCode().trim();
        String destinationCode = request.getDestinationCode().trim();

        // Evita transferencias sin efecto entre la misma ubicacion.
        if (originCode.equalsIgnoreCase(destinationCode)) {
            throw new RuntimeException("Origen y destino no pueden ser iguales");
        }

        Location origin = locationRepository.findByCodeIgnoreCase(originCode)
                .orElseThrow(() -> new RuntimeException("Ubicación origen no encontrada"));

        Location destination = locationRepository.findByCodeIgnoreCase(destinationCode)
                .orElseThrow(() -> new RuntimeException("Ubicación destino no encontrada"));

        // La transferencia solo se permite hacia una ubicacion vacia.
        boolean destinationOccupied = stockRepository
                .existsByLocationAndQuantityGreaterThan(destination, 0);

        if (destinationOccupied) {
            throw new RuntimeException("La ubicación destino no está vacía");
        }

        // Obtiene todos los registros con stock positivo en la ubicacion origen.
        List<Stock> stocksToTransfer = stockRepository
                .findByLocationAndQuantityGreaterThan(origin, 0);

        if (stocksToTransfer.isEmpty()) {
            throw new RuntimeException("La ubicación origen no tiene stock disponible");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        InventoryMovement movement = new InventoryMovement();
        movement.setType("TRANSFER");
        movement.setCreatedBy(user);

        // Mueve cada registro de stock y crea una linea de movimiento asociada.
        for (Stock stock : stocksToTransfer) {

            InventoryMovementLine line = new InventoryMovementLine();
            line.setMovement(movement);
            line.setProduct(stock.getProduct());
            line.setFromLocation(origin);
            line.setToLocation(destination);
            line.setQuantity(stock.getQuantity());

            movement.getLines().add(line);

            stock.setLocation(destination);
            stockRepository.save(stock);
        }

        movementRepository.save(movement);
    }
}
