package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CheckoutValidationResponse {

    private boolean valid;

    private List<String> errors;

    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private String couponCode;
    private BigDecimal shippingCost;
    private String shippingMethod;
    private BigDecimal total;
}