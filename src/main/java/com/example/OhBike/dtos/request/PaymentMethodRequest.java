package com.example.OhBike.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentMethodRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre no debe de exceder los 50 caracteres")
        String name,

        @Size(max = 255, message = "La descripcion no debe de exceder 255 caracteres")
        String description
) {}
