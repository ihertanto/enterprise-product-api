package com.ubl.productapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
    @NotBlank(message = "name is required")
    private String name;

    @Positive(message = "price must be greater than 0")
    private double price;

    @Min(value = 0, message = "stock cannot be negative")
    private int stock;

    private Long categoryId;
}