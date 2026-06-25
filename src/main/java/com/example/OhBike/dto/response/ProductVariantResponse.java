package com.example.OhBike.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductVariantResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String size;
    private String color;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private Integer minStock;
    private Boolean active;
    private Boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}