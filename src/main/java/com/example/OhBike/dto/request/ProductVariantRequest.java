package com.example.OhBike.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotBlank(message = "Size is required")
    @Size(max = 100, message = "Size cannot exceed 100 characters")
    private String size;

    @NotBlank(message = "Color is required")
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    private String color;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\-_]+$", message = "SKU can only contain letters, numbers, hyphens and underscores")
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Minimum stock is required")
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minStock;
}