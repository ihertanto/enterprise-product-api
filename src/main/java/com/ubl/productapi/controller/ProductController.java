package com.ubl.productapi.controller;

import com.ubl.productapi.dto.*;
import com.ubl.productapi.mapper.ProductMapper;
import com.ubl.productapi.model.Product;
import com.ubl.productapi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    private final ProductMapper mapper;

    public ProductController(ProductService service, ProductMapper mapper) {
    	this.service = service;
		this.mapper = mapper;
    }

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return service.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable("id") Long id){
        return mapper.toResponseDTO(service.findById(id));
    }


    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO dto){
        Product saved = service.create(mapper.toEntity(dto));
        ProductResponseDTO resp = mapper.toResponseDTO(saved);
        return ResponseEntity.created(URI.create("/api/products/" + resp.getId())).body(resp);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable("id") Long id, @Valid @RequestBody ProductRequestDTO dto){
        Product updated = service.update(id, mapper.toEntity(dto));
        return mapper.toResponseDTO(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
