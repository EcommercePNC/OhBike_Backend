package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {

        @NotNull(message = "The product variant ID is required")
        private UUID productVariantId;

        @NotNull(message = "The amount is obligatory")
        @Positive(message = "The amount must be greater than zero")
        private Integer quantity;
}