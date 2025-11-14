package com.ubl.productapi.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ubl.productapi.dto.CategoryRequestDTO;
import com.ubl.productapi.dto.CategoryResponseDTO;
import com.ubl.productapi.dto.CategoryWithProductsRequestDTO;
import com.ubl.productapi.dto.ProductRequestDTO;
import com.ubl.productapi.dto.ProductResponseDTO;
import com.ubl.productapi.model.Category;
import com.ubl.productapi.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toResponseDTO(Product entity);

    CategoryResponseDTO toCategoryResponseDTO(Category c);

    Category toCategory(CategoryRequestDTO dto);

    @Mapping(target = "products", source = "products")
    Category toCategory(CategoryWithProductsRequestDTO dto);

    @AfterMapping
    default void linkProductsToCategory(CategoryWithProductsRequestDTO dto, @MappingTarget Category category) {
        if (category == null) return;
        if (category.getProducts() == null) return;
        for (Product p : category.getProducts()) {
            // ensure each product has the parent category set
            p.setCategory(category);
        }
    }
}