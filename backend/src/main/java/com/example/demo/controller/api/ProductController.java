
package com.example.demo.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

/**
 * Controlador REST que expone operaciones CRUD sobre productos.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Devuelve todos los productos del sistema.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(products);
    }

    /**
     * Busca un producto por su identificador y lanza una excepcion si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(productService.getProductById(id)));
    }

    /**
     * Crea un producto nuevo a partir de los datos recibidos en la peticion.
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        Product savedProduct = productService.createProduct(request);
        return ResponseEntity.ok(toResponse(savedProduct));
    }

    /**
     * Actualiza los datos de un producto existente manteniendo su identificador.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        Product updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(toResponse(updatedProduct));
    }

    /**
     * Elimina un producto por su identificador.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte una entidad del dominio en un DTO seguro para la respuesta de la API.
     */
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getUnit(),
                product.getMinStock(),
                product.getCriticality(),
                product.getCategory().getId().longValue(),
                product.getCategory().getName()
        );
    }
}
