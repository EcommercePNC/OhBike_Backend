package com.example.OhBike.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderDetailResponse(
        UUID id,
        UUID productVariantId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
