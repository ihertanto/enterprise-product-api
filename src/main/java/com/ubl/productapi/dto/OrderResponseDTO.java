package com.ubl.productapi.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private LocalDateTime orderDate;

    // latest available stock of the product after the order
    private int productStock;
}