package com.ubl.productapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ubl.productapi.dto.ProductRequestDTO;
import com.ubl.productapi.dto.ProductResponseDTO;
import com.ubl.productapi.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toResponseDTO(Product entity);
}