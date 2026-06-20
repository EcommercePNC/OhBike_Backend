package com.example.OhBike.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {
    @NotBlank(message = "El código no puede estar vacío")
    @Size(min = 3, max = 10, message = "El código debe cumplir con el numero de caracteres")
    private String code;

    @NotNull(message = "El número máximo de usos es obligatorio")
    @Min(value = 1, message = "El cupón debe permitir al menos 1 uso")
    private Integer maxUses;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @FutureOrPresent(message = "La fecha de expiración debe ser hoy o una fecha cercana")
    private LocalDate expirationDate;

    @NotNull(message = "El ID del descuento es obligatorio")
    private UUID discountId;

}
