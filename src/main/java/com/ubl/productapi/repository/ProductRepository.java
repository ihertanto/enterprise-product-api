package com.ubl.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ubl.productapi.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
