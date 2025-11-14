package com.ubl.productapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithProductsRequestDTO {
    @NotBlank(message = "name is required")
    private String name;

    private List<ProductRequestDTO> products;
}
