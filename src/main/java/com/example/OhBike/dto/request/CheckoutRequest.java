package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    @NotNull(message = "Payment method is required")
    private UUID paymentMethodId;

    @NotNull(message = "Shipping method is required")
    private UUID shippingMethodId;

    private String couponCode;
}