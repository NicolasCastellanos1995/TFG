package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Devuelve todos los productos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Busca un producto por su ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Busca productos por categoría
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Busca productos por nombre
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Busca productos por ubicación
    public List<Product> getProductsByLocation(Long locationId) {
        return productRepository.findProductsByLocationId(locationId);
    }

    // Guarda o actualiza un producto
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Elimina un producto por ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}