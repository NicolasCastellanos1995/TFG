package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ProductExitRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.InventoryMovementLine;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

/**
 * Servicio que procesa salidas de producto del almacen.
 * Aplica una estrategia FIFO por fecha de caducidad para consumir primero los lotes que caducan antes.
 */
@Service
public class ProductExitService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final InventoryMovementRepository movementRepository;
    private final UserRepository userRepository;

    public ProductExitService(
            ProductRepository productRepository,
            StockRepository stockRepository,
            InventoryMovementRepository movementRepository,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.movementRepository = movementRepository;
        this.userRepository = userRepository;
    }

    /**
     * Procesa una salida de producto aplicando FIFO por caducidad y registra el movimiento.
     */
    @Transactional
    public void processExit(ProductExitRequest request, String username) {

        // Valida los datos minimos necesarios antes de consultar la base de datos.
        if (request.getProduct() == null || request.getProduct().trim().isEmpty()) {
            throw new RuntimeException("Debe indicar el producto por SKU o nombre");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que 0");
        }

        String productText = request.getProduct().trim();

        Product product = productRepository
                .findFirstBySkuIgnoreCaseOrNameIgnoreCase(productText, productText)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Recupera los lotes con stock positivo ordenados por fecha de caducidad para aplicar FIFO.
        List<Stock> lots = stockRepository
                .findByProductAndQuantityGreaterThanOrderByExpirationDateAscIdAsc(product, 0);

        // Calcula el stock total disponible para garantizar que la salida puede completarse.
        int totalAvailable = lots.stream()
                .mapToInt(stock -> stock.getQuantity() != null ? stock.getQuantity() : 0)
                .sum();

        if (totalAvailable < request.getQuantity()) {
            throw new RuntimeException(
                    "Stock insuficiente. Disponible: " + totalAvailable
            );
        }

        InventoryMovement movement = new InventoryMovement();
        movement.setType("OUT");
        movement.setCreatedBy(user);

        int remaining = request.getQuantity();

        // Descuenta unidades lote a lote hasta cubrir la cantidad solicitada.
        for (Stock stock : lots) {

            if (remaining <= 0) {
                break;
            }

            int availableInLot = stock.getQuantity();
            int quantityToTake = Math.min(availableInLot, remaining);

            stock.setQuantity(availableInLot - quantityToTake);
            stockRepository.save(stock);

            // Registra una linea de movimiento por cada lote consumido.
            InventoryMovementLine line = new InventoryMovementLine();
            line.setMovement(movement);
            line.setProduct(product);
            line.setFromLocation(stock.getLocation());
            line.setToLocation(null);
            line.setQuantity(quantityToTake);

            movement.getLines().add(line);

            remaining -= quantityToTake;
        }

        movementRepository.save(movement);
    }
}
