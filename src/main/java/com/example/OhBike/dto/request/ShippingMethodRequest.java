package com.example.OhBike.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ShippingMethodRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre no debe de exceder los 50 caracteres")
        String name,

        @NotNull(message = "El precio es requerido")
        @PositiveOrZero(message = "El precio debe de ser cero o positivo")
        BigDecimal baseCost
) {}
