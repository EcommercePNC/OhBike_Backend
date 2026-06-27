package com.example.OhBike.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPublicResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Boolean available;
    private String categoryName;
}