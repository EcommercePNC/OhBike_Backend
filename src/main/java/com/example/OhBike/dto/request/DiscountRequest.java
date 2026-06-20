package com.example.OhBike.dto.request;

import com.example.OhBike.entities.enums.DiscountType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    @NotBlank(message = "El nombre del descuento es obligatorio")
    private String name;

    @NotNull(message = "El tipo de descuento es obligatorio")
    private DiscountType discountType;

    @NotNull(message = "El valor del descuento es obligatorio")
    @Min(value = 0, message = "El valor debe ser mayor a 0")
    private Float value;

    @NotNull(message = "El estado del descuento es obligatorio")
    private Boolean active;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de caducidad es obligatoria")
    @FutureOrPresent(message = "La fecha de fin debe ser en el presente o proxima")
    private LocalDate endDate;
}
