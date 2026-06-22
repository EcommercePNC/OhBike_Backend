package com.example.OhBike.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID userId,
        String userName,
        String couponCode,
        UUID shippingMethodId,
        LocalDateTime orderDate,
        BigDecimal subtotal,
        BigDecimal shippingCost,
        BigDecimal discountTotal,
        String status,
        BigDecimal total,
        List<OrderDetailResponse> items
) {}
