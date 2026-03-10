package com.example.demo.controller.api;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    // Service que contiene la lógica de negocio de productos
    private final ProductService productService;

    // Inyección por constructor
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /products
     * Devuelve todos los productos
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * GET /products/{id}
     * Devuelve un producto según su id
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * GET /products/category/{categoryId}
     * Devuelve todos los productos de una categoría
     */
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    /**
     * GET /products/name/{name}
     * Busca productos por nombre
     */
    @GetMapping("/name/{name}")
    public List<Product> getProductsByName(@PathVariable String name) {
        return productService.getProductsByName(name);
    }

    /**
     * GET /products/location/{locationId}
     * Devuelve productos que tienen stock en una ubicación concreta
     */
    @GetMapping("/location/{locationId}")
    public List<Product> getProductsByLocation(@PathVariable Long locationId) {
        return productService.getProductsByLocation(locationId);
    }

    /**
     * POST /products
     * Guarda un producto nuevo
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    /**
     * PUT /products/{id}
     * Actualiza un producto existente
     */
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.saveProduct(product);
    }

    /**
     * DELETE /products/{id}
     * Elimina un producto por id
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}