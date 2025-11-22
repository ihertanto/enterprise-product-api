package com.ubl.productapi.service;

import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.ProductRepository;
import com.ubl.productapi.specification.ProductSpec;
import com.ubl.productapi.dto.ProductResponseDTO;
import com.ubl.productapi.exception.ResourceNotFoundException;
import com.ubl.productapi.mapper.ProductMapper;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
	private final ProductRepository repo;
	private final ProductMapper mapper;

	public ProductService(ProductRepository repo, ProductMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	public List<Product> findAll() {
		return repo.findAll();
	}

	public Product findById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
	}

	public Product create(Product p) {
		return repo.save(p);
	}

	public Product update(Long id, Product updated) {
		Product exist = findById(id);
		exist.setName(updated.getName());
		exist.setPrice(updated.getPrice());
		exist.setStock(updated.getStock());
		exist.setCategory(updated.getCategory());
		return repo.save(exist);
	}

	public void delete(Long id) {
		Product exist = findById(id);
		repo.delete(exist);
	}

	// =================================================================
	// SEARCH PRODUCT (Dynamic Filter + Pagination + Sorting)
	// =================================================================
	public Page<ProductResponseDTO> search(String keyword, String categoryName, Double minPrice, Double maxPrice,
			int page, int size, String sortBy, String direction) {

		Pageable pageable = PageRequest.of(page, size,
				"desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		// Build Specification (dynamic filter)
		Specification<Product> spec = Specification.where(ProductSpec.nameContains(keyword))
				.and(ProductSpec.hasCategory(categoryName)).and(ProductSpec.priceBetween(minPrice, maxPrice));

		return repo.findAll(spec, pageable).map(mapper::toResponseDTO);
	}

}