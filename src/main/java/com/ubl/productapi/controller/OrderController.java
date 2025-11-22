package com.ubl.productapi.controller;

import com.ubl.productapi.dto.OrderRequestDTO;
import com.ubl.productapi.dto.OrderResponseDTO;
import com.ubl.productapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO resp = service.createOrder(dto);
        return ResponseEntity.created(URI.create("/api/orders/" + resp.getId())).body(resp);
    }
}
