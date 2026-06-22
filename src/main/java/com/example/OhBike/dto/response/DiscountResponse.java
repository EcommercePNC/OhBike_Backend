package com.example.OhBike.dto.response;

import com.example.OhBike.entity.enums.DiscountType;
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
public class DiscountResponse {
    private UUID id;
    private String name;
    private DiscountType discountType;
    private Float value;
    private Boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
}
