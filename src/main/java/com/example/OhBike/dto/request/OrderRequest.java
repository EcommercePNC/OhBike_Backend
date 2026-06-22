package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
        @NotNull(message = "User ID is required")
        UUID userId,

        UUID couponId,

        @NotNull(message = "The shipping method ID is required")
        UUID shippingMethodId,

        @NotEmpty(message = "The order must contain at least one item in the details")
        List<OrderDetailRequest> items
) {}
