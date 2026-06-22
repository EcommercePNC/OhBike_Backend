package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentMethodRequest(
        @NotBlank(message = "The name is required")
        @Size(max = 50, message = "The name must not exceed 50 characters")
        String name,

        @Size(max = 255, message = "The description should not exceed 255 characters")
        String description
) {}
