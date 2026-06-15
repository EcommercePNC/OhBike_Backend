package com.example.OhBike.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ShippingMethodResponse(
        UUID id,
        String name,
        BigDecimal baseCost
) {}
