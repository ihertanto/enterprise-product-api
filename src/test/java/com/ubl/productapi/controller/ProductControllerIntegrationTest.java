package com.ubl.productapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubl.productapi.dto.ProductRequestDTO;
import com.ubl.productapi.model.Category;
import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.CategoryRepository;
import com.ubl.productapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Category cat;

    @BeforeEach
    void setup() {
        productRepo.deleteAll();
        categoryRepo.deleteAll();
        cat = categoryRepo.save(Category.builder().name("Cat1").build());
    }

    @Test
    void createProduct_returnsCreated() throws Exception {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("ProdA");
        dto.setPrice(9.9);
        dto.setStock(10);
        dto.setCategoryId(cat.getId());

        mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("ProdA"));
    }

    @Test
    void getAll_returnsList() throws Exception {
        productRepo.save(Product.builder().name("X").price(1.0).stock(1).category(cat).build());

        mvc.perform(get("/api/products")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("X"));
    }
}
