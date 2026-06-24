package com.example.OhBike.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CartItemResponse {

    private UUID cartItemId;
    private UUID variantId;
    private String productName;
    private String size;
    private String color;
    private String sku;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}