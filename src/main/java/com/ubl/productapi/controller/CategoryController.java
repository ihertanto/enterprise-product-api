package com.ubl.productapi.controller;

import com.ubl.productapi.dto.*;
import com.ubl.productapi.mapper.ProductMapper;
import com.ubl.productapi.model.Category;
import com.ubl.productapi.model.Product;
import com.ubl.productapi.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;
    private final ProductMapper mapper;

    public CategoryController(CategoryService service, ProductMapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<CategoryResponseDTO> getAll(){
        return service.findAll().stream().map(mapper::toCategoryResponseDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO getById(@PathVariable("id") Long id){
        Category category = service.findById(id);
        return mapper.toCategoryResponseDTO(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO dto){
        Category toSave = mapper.toCategory(dto);
        Category saved = service.create(toSave);
        CategoryResponseDTO resp = mapper.toCategoryResponseDTO(saved);
        return ResponseEntity.created(URI.create("/api/categories/" + resp.getId())).body(resp);
    }

    @PostMapping("/with-products")
    public ResponseEntity<CategoryResponseDTO> createWithProducts(@Valid @RequestBody CategoryWithProductsRequestDTO dto){
        Category cat = mapper.toCategory(dto);
        List<Product> products = dto.getProducts() == null ? List.of() : dto.getProducts().stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        Category saved = service.addCategoryWithProducts(cat, products);
        CategoryResponseDTO resp = mapper.toCategoryResponseDTO(saved);
        return ResponseEntity.created(URI.create("/api/categories/" + resp.getId())).body(resp);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequestDTO dto){
        Category toUpdate = mapper.toCategory(dto);
        Category updated = service.update(id, toUpdate);
        return mapper.toCategoryResponseDTO(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}