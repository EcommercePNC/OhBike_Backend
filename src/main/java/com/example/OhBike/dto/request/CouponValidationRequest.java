package com.example.OhBike.dto.request;

import lombok.Data;

@Data
public class CouponValidationRequest {
    private String code;
    private Double purchaseAmount;
}