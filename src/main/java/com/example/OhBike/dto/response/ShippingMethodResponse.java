package com.example.OhBike.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ShippingMethodResponse(
        UUID id,
        String name,
        BigDecimal baseCost
) {}
