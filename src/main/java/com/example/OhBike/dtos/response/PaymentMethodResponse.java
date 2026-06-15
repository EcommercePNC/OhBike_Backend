package com.example.OhBike.dtos.response;

import java.util.UUID;

public record PaymentMethodResponse(
        UUID id,
        String name,
        String description
) {}
