package com.dev.springcrud.mapper;

import com.dev.springcrud.dto.ProductDTO;
import com.dev.springcrud.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toEntity(ProductDTO productDTO);
}