package com.ubl.productapi.service;

import com.ubl.productapi.model.Category;
import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.CategoryRepository;
import com.ubl.productapi.repository.ProductRepository;
import com.ubl.productapi.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository repo;
    private final ProductRepository productRepo;

    public CategoryService(CategoryRepository repo, ProductRepository productRepo){
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public List<Category> findAll(){
        return repo.findAll();
    }

    public Category findById(Long id){
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    public Category create(Category c){
        return repo.save(c);
    }

    public Category update(Long id, Category updated){
        Category exist = findById(id);
        exist.setName(updated.getName());
        return repo.save(exist);
    }

    public void delete(Long id){
        Category exist = findById(id);
        repo.delete(exist);
    }

    @Transactional
    public Category addCategoryWithProducts(Category cat, List<Product> products){
        Category savedCat = repo.save(cat);
        if (products != null && !products.isEmpty()){
            List<Product> toSave = products.stream().map(p -> {
                p.setCategory(savedCat);
                return p;
            }).collect(Collectors.toList());
            productRepo.saveAll(toSave);
            savedCat.setProducts(toSave);
        }
        return savedCat;
    }
}