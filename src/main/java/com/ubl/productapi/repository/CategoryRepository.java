package com.ubl.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ubl.productapi.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
