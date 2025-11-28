package com.ubl.productapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.ProductRepository;
import com.ubl.productapi.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    private Product sample;

    @BeforeEach
    void init() {
        sample = new Product();
        sample.setName("Laptop");
    }

    @Test
    void testCreateProduct() {
        when(repo.save(sample)).thenReturn(sample);

        Product result = service.create(sample);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());

        verify(repo, times(1)).save(sample);
    }
    
    @Test
    void testUpdateProduct() {
    	Long id = 1L;
    	
    	when(repo.findById(id)).thenReturn(Optional.of(sample));
    	when(repo.save(sample)).thenReturn(sample);
    	
    	Product result = service.update(id, sample);
    	assertNotNull(result);
    }
    
}
