package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
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
    private UUID sellerId;
    private String sellerName;

    private List<ProductVariantResponse> variants;
}
