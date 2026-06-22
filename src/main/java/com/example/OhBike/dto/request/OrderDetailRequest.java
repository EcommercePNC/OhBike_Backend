package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record OrderDetailRequest(
        @NotNull(message = "The product variant ID is required")
        java.util.UUID productVariantId,

        @NotNull(message = " The amount is obligatory")
        @Positive(message = "The amount must be greater than zero")
        Integer quantity
) {}
