package com.example.OhBike.dto.response;

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
public class CouponResponse {
    private UUID id;
    private String code;
    private Integer maxUses;
    private Integer usedCount;
    private LocalDate expirationDate;
    private UUID discountId;
}