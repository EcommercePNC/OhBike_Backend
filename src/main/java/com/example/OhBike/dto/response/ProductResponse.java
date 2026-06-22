package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Boolean available;
    private ProductCategoryResponse category;
}
