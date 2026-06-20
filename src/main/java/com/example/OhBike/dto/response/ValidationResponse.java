package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ValidationResponse {
    private String code;
    private Boolean isValid;
    private String message;
    private Double discountValue;
    private String discountType;
}