package com.dev.springcrud.controller;

import com.dev.springcrud.dto.ProductDTO;
import com.dev.springcrud.exception.ProductNotFoundException;
import com.dev.springcrud.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        logger.info("Creating new product with name: {}", productDTO.getName());
        ProductDTO savedProduct = productService.saveProduct(productDTO);
        logger.info("Product created with ID: {}", savedProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductDTO>> createProducts(@Valid @RequestBody List<ProductDTO> productDTOs) {
        logger.info("Creating products in batch. Number of products: {}", productDTOs.size());
        List<ProductDTO> savedProducts = productService.saveProducts(productDTOs);
        logger.info("Products created. Number of products saved: {}", savedProducts.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        logger.info("Fetching all products");
        List<ProductDTO> products = productService.getAllProducts();
        logger.info("Number of products fetched: {}", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        logger.info("Fetching product with ID: {}", id);
        return productService.getProductById(id)
                .map(productDTO -> {
                    logger.info("Product found with ID: {}", id);
                    return ResponseEntity.ok(productDTO);
                })
                .orElseThrow(() -> {
                    logger.warn("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with id " + id);
                });
    }

    @GetMapping("/search")
    public ResponseEntity<ProductDTO> getProductByName(@RequestParam String name) {
        logger.info("Fetching product with name: {}", name);
        return productService.getProductByName(name)
                .map(productDTO -> {
                    logger.info("Product found with name: {}", name);
                    return ResponseEntity.ok(productDTO);
                })
                .orElseThrow(() -> {
                    logger.warn("Product not found with name: {}", name);
                    return new ProductNotFoundException("Product not found with name " + name);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        logger.info("Deleting product with ID: {}", id);
        productService.removeProduct(id);
        logger.info("Product deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDTO productDTO) {
        logger.info("Updating product with ID: {}", id);
        return productService.updateProduct(id, productDTO)
                .map(updatedProductDTO -> {
                    logger.info("Product updated with ID: {}", id);
                    return ResponseEntity.ok(updatedProductDTO);
                })
                .orElseThrow(() -> {
                    logger.warn("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with id " + id);
                });
    }
}
