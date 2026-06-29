package com.example.OhBike.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderDetailResponse {

    private UUID orderDetailId;
    private UUID variantId;
    private String productName;
    private String size;
    private String color;
    private String sku;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}