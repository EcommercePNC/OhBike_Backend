package com.example.OhBike.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductVariantRequest {

    @Size(max = 100)
    private String size;

    @Size(max = 50)
    private String color;

    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-z0-9\\-_]+$", message = "SKU can only contain letters, numbers, hyphens and underscores")
    private String sku;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minStock;
}