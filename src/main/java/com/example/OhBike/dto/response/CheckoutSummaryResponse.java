package com.example.OhBike.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CheckoutSummaryResponse {

    private List<CartItemResponse> items;
    private BigDecimal subtotal;
    private String couponCode;
    private BigDecimal discountAmount;
    private String shippingMethod;
    private BigDecimal shippingCost;
    private BigDecimal total;
}