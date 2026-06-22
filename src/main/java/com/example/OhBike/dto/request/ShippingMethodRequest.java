package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ShippingMethodRequest(
        @NotBlank(message = "The name is required")
        @Size(max = 50, message = "The name must not exceed 50 characters")
        String name,

        @NotNull(message = "The price is required")
        @PositiveOrZero(message = "The price must be zero or positive.")
        BigDecimal baseCost
) {}
