package com.example.OhBike.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productId;

    @NotBlank(message = "La talla/tamaño es obligatoria")
    @Size(max = 100, message = "La talla no puede superar 100 caracteres")
    private String size;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 50, message = "El color no puede superar 50 caracteres")
    private String color;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 100, message = "El SKU no puede superar 100 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\-_]+$", message = "El SKU solo puede contener letras, números, guiones y guiones bajos")
    private String sku;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer minStock;
}