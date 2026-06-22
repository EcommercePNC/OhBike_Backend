package com.example.OhBike.dto.request;

import com.example.OhBike.entity.enums.DiscountType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    @NotBlank(message = "The name is mandatory")
    private String name;

    @NotNull(message = "The discount type is mandatory")
    private DiscountType discountType;

    @NotNull(message = "The value is mandatory")
    @Min(value = 0, message = "The value must be greater than or equal to 0")
    private BigDecimal value;

    @NotNull(message = "The discount status is mandatory")
    private Boolean active;

    @NotNull(message = "The start date is mandatory")
    private LocalDate startDate;

    @NotNull(message = "The end date is mandatory")
    @FutureOrPresent(message = "The end date must be present or in the future")
    private LocalDate endDate;
}
