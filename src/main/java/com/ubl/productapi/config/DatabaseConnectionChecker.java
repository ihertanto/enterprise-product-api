package com.ubl.productapi.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import com.ubl.productapi.repository.ProductRepository;

@Component
public class DatabaseConnectionChecker {

    private final ProductRepository productRepository;

    public DatabaseConnectionChecker(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void checkDatabaseConnection() {
        long count = productRepository.count();
        System.out.println("Database connection verified. Product count = " + count);
    }
}
