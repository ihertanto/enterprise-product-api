package com.ubl.productapi.service;

import com.ubl.productapi.dto.OrderRequestDTO;
import com.ubl.productapi.dto.OrderResponseDTO;
import com.ubl.productapi.exception.ResourceNotFoundException;
import com.ubl.productapi.model.Order;
import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.OrderRepository;
import com.ubl.productapi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public OrderService(ProductRepository productRepo, OrderRepository orderRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // lock product for update
        Product product = productRepo.findForUpdate(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + dto.getProductId()));

        if (product.getStock() < dto.getQuantity()) {
            throw new IllegalStateException("Insufficient stock for product id " + dto.getProductId());
        }

        // update stock
        product.setStock(product.getStock() - dto.getQuantity());
        productRepo.save(product);

        // save order
        Order order = Order.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .orderDate(LocalDateTime.now())
                .build();

        Order saved = orderRepo.save(order);

        return OrderResponseDTO.builder()
                .id(saved.getId())
                .productId(product.getId())
                .quantity(saved.getQuantity())
                .orderDate(saved.getOrderDate())
                .productStock(product.getStock())
                .build();
    }
}