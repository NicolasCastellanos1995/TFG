package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

/**
 * Servicio principal de gestion de productos.
 * Centraliza las operaciones de consulta, creacion, actualizacion y eliminacion.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Devuelve todos los productos del sistema.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto por su identificador y lanza una excepcion si no existe.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByLocation(Long locationId) {
        return productRepository.findProductsByLocationId(locationId);
    }

    /**
     * Crea un producto nuevo a partir de los datos recibidos en la peticion.
     */
    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setCategory(category);
        product.setUnit(request.getUnit());
        product.setMinStock(request.getMinStock());
        product.setCriticality(request.getCriticality());

        return productRepository.save(product);
    }

    /**
     * Actualiza los datos de un producto existente manteniendo su identificador.
     */
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setCategory(category);
        product.setUnit(request.getUnit());
        product.setMinStock(request.getMinStock());
        product.setCriticality(request.getCriticality());

        return productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Elimina un producto por su identificador.
     */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
