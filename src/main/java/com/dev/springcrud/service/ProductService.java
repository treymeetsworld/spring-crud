package com.dev.springcrud.service;

import com.dev.springcrud.dto.ProductDTO;
import com.dev.springcrud.entity.Product;
import com.dev.springcrud.mapper.ProductMapper;
import com.dev.springcrud.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductDTO saveProduct(@Valid ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Transactional
    public List<ProductDTO> saveProducts(List<ProductDTO> productDTOs) {
        List<Product> products = productDTOs.stream()
                .map(productMapper::toEntity)
                .collect(Collectors.toList());
        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(int productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDTO);
    }

    public Optional<ProductDTO> getProductByName(String productName) {
        return Optional.ofNullable(productRepository.findByName(productName))
                .map(productMapper::toDTO);
    }

    @Transactional
    public void removeProduct(int productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public Optional<ProductDTO> updateProduct(int id, @Valid ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductDetails(existingProduct, productDTO);
                    Product updatedProduct = productRepository.save(existingProduct);
                    return productMapper.toDTO(updatedProduct);
                });
    }

    private void updateProductDetails(Product existingProduct, ProductDTO productDTO) {
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
    }
}