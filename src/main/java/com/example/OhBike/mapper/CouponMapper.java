package com.example.OhBike.mapper;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.CouponResponse;
import com.example.OhBike.entities.Coupon;
import com.example.OhBike.entities.Discount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponMapper {

    private final DiscountMapper discountMapper;

    public Coupon toEntityCreate(CouponRequest request, Discount discount) {
        return Coupon.builder()
                .code(request.getCode())
                .expirationDate(request.getExpirationDate())
                .maxUses(request.getMaxUses())
                .usedCount(0)
                .discount(discount)
                .build();
    }

    public Coupon toEntityUpdate(CouponRequest request, Coupon existingCoupon) {
        if (request.getCode() != null && !request.getCode().isBlank()) {
            existingCoupon.setCode(request.getCode());
        }
        if (request.getMaxUses() != null) {
            existingCoupon.setMaxUses(request.getMaxUses());
        }
        if (request.getExpirationDate() != null) {
            existingCoupon.setExpirationDate(request.getExpirationDate());
        }
        return existingCoupon;
    }

    public CouponResponse toDto(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .maxUses(coupon.getMaxUses())
                .usedCount(coupon.getUsedCount())
                .expirationDate(coupon.getExpirationDate())
                .discountId(coupon.getDiscount().getId())
                .build();
    }
}