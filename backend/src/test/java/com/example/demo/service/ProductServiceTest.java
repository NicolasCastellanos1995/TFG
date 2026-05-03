package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.ProductRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

/**
 * Pruebas unitarias de ProductService.
 * Verifican el comportamiento esperado sin modificar la logica de produccion.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    /**
     * Comprueba el caso de prueba: createProductBuildsProductFromRequestAndSavesIt.
     */
    @Test
    void createProductBuildsProductFromRequestAndSavesIt() {
        Category category = new Category();
        category.setId(3L);
        category.setName("Material sanitario");

        ProductRequest request = new ProductRequest();
        request.setSku("SKU-001");
        request.setName("Guantes");
        request.setCategoryId(3L);
        request.setUnit("caja");
        request.setMinStock(20);
        request.setCriticality("ALTA");

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product savedProduct = productService.createProduct(request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product productSentToRepository = captor.getValue();
        assertSame(productSentToRepository, savedProduct);
        assertEquals("SKU-001", productSentToRepository.getSku());
        assertEquals("Guantes", productSentToRepository.getName());
        assertSame(category, productSentToRepository.getCategory());
        assertEquals("caja", productSentToRepository.getUnit());
        assertEquals(20, productSentToRepository.getMinStock());
        assertEquals("ALTA", productSentToRepository.getCriticality());
    }

    /**
     * Comprueba el caso de prueba: createProductThrowsExceptionWhenCategoryDoesNotExist.
     */
    @Test
    void createProductThrowsExceptionWhenCategoryDoesNotExist() {
        ProductRequest request = new ProductRequest();
        request.setCategoryId(99L);

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.createProduct(request));

        assertEquals("Categoría no encontrada", exception.getMessage());
    }

    /**
     * Comprueba el caso de prueba: updateProductChangesExistingProductAndSavesIt.
     */
    @Test
    void updateProductChangesExistingProductAndSavesIt() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setSku("OLD");

        Category category = new Category();
        category.setId(2L);

        ProductRequest request = new ProductRequest();
        request.setSku("NEW");
        request.setName("Mascarillas");
        request.setCategoryId(2L);
        request.setUnit("unidad");
        request.setMinStock(5);
        request.setCriticality("MEDIA");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.updateProduct(1L, request);

        assertSame(existingProduct, result);
        assertEquals("NEW", existingProduct.getSku());
        assertEquals("Mascarillas", existingProduct.getName());
        assertSame(category, existingProduct.getCategory());
        assertEquals("unidad", existingProduct.getUnit());
        assertEquals(5, existingProduct.getMinStock());
        assertEquals("MEDIA", existingProduct.getCriticality());
        verify(productRepository).save(existingProduct);
    }
}
