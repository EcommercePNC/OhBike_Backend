package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartRefreshResponse {

    private List<CartItemResponse> items;

    private List<String> removedItems;

    private List<String> adjustedItems;

    private BigDecimal subtotal;
    private int totalItems;
    private boolean wasModified;
}