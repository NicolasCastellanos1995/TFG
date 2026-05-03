package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductEntryRequest;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.InventoryMovementLine;
import com.example.demo.entity.Location;
import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.entity.User;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

/**
 * Servicio que procesa entradas de producto en el almacen.
 * Selecciona la mejor ubicacion vacia segun la criticidad del producto y registra el movimiento de entrada.
 */
@Service
public class ProductEntryService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final InventoryMovementRepository movementRepository;
    private final UserRepository userRepository;

    public ProductEntryService(ProductRepository productRepository,
                               LocationRepository locationRepository,
                               StockRepository stockRepository,
                               InventoryMovementRepository movementRepository,
                               UserRepository userRepository) {
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.stockRepository = stockRepository;
        this.movementRepository = movementRepository;
        this.userRepository = userRepository;
    }

    /**
     * Procesa una entrada de producto, asigna ubicacion, guarda el stock y registra el movimiento.
     */
    public void processEntry(ProductEntryRequest request, String username) {

        // Valida los datos obligatorios de la entrada.
        if (request.getProduct() == null || request.getProduct().trim().isEmpty()) {
            throw new RuntimeException("Debe indicar el producto por SKU o nombre");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que 0");
        }

        if (request.getLotCode() == null || request.getLotCode().trim().isEmpty()) {
            throw new RuntimeException("Debe indicar el lote");
        }

        
        String productText = request.getProduct().trim();

        // Localiza el producto permitiendo que el usuario introduzca SKU o nombre.
        Product product = productRepository
                .findFirstBySkuIgnoreCaseOrNameIgnoreCase(productText, productText)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado por SKU o nombre"));

        if (product.getCriticality() == null || product.getCriticality().trim().isEmpty()) {
            throw new RuntimeException("El producto no tiene criticidad asignada");
        }

        String productCriticality = product.getCriticality().trim();

        // Selecciona una ubicacion vacia siguiendo la prioridad definida por criticidad.
        Location selectedLocation = findBestEmptyLocation(productCriticality);

        // Crea el registro de stock con producto, ubicacion, lote, cantidad y caducidad.
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setLocation(selectedLocation);
        stock.setQuantity(request.getQuantity());
        stock.setLotCode(request.getLotCode().trim());
        stock.setExpirationDate(request.getExpirationDate());
        stockRepository.save(stock);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Registra el movimiento para conservar la trazabilidad de la operacion.
        InventoryMovement movement = new InventoryMovement();
        movement.setType("IN");
        movement.setCreatedBy(user);

        InventoryMovementLine line = new InventoryMovementLine();
        line.setMovement(movement);
        line.setProduct(product);
        line.setFromLocation(null);
        line.setToLocation(selectedLocation);
        line.setQuantity(request.getQuantity());

        movement.getLines().add(line);

        movementRepository.save(movement);
    }

    /**
     * Localiza la mejor ubicacion vacia disponible segun la criticidad del producto.
     */
    private Location findBestEmptyLocation(String productCriticality) {

        List<String> priority = getCriticalityFallbackOrder(productCriticality);

        // Recorre las criticidades permitidas hasta encontrar una ubicacion libre.
        for (String criticality : priority) {

            List<Location> locations = locationRepository.findByCriticalityIgnoreCase(criticality);

            for (Location location : locations) {

                boolean isEmpty = !stockRepository.existsByLocationAndQuantityGreaterThan(location, 0);

                if (isEmpty) {
                    return location;
                }
            }
        }

        throw new RuntimeException(
                "No hay ubicaciones vacías disponibles para criticidad: " + productCriticality
        );
    }

    /**
     * Devuelve el orden de prioridad de ubicaciones segun la criticidad del producto.
     */
    private List<String> getCriticalityFallbackOrder(String criticality) {

        String normalized = criticality.trim().toUpperCase();

        switch (normalized) {
            case "ALTA":
                return List.of("ALTA", "MEDIA", "BAJA");

            case "MEDIA":
                return List.of("MEDIA", "BAJA");

            case "BAJA":
                return List.of("BAJA");

            default:
                throw new RuntimeException("Criticidad no válida: " + criticality);
        }
    }
}
