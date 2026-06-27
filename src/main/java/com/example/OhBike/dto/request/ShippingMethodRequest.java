package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ShippingMethodRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @NotNull(message = "Base cost is required")
        @PositiveOrZero(message = "Base cost must be zero or greater")
        BigDecimal baseCost
) {}
