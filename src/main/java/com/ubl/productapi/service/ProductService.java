package com.ubl.productapi.service;

import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.ProductRepository;
import com.ubl.productapi.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo){
        this.repo = repo;
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

}
