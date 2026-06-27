package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingMethodRequest {

        @NotBlank(message = "The name is required")
        @Size(max = 50, message = "The name must not exceed 50 characters")
        private String name;

        @NotNull(message = "The price is required")
        @PositiveOrZero(message = "The price must be zero or positive.")
        private BigDecimal baseCost;
}
