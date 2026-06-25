package com.example.OhBike.dto.response;

import java.util.UUID;

public record PaymentMethodResponse(
        UUID id,
        String name,
        String description
) {}
