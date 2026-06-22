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
    @NotBlank(message = "The code cannot be empty")
    @Size(min = 3, max = 10, message = "The code must be between 3 and 10 characters")
    private String code;

    @NotNull(message = "The maximum number of uses is mandatory")
    @Min(value = 1, message = "The coupon must allow at least 1 use")
    private Integer maxUses;

    @NotNull(message = "The expiration date is mandatory")
    @FutureOrPresent(message = "The expiration date must be today or a future date")
    private LocalDate expirationDate;

    @NotNull(message = "The discount ID is mandatory")
    private UUID discountId;
}