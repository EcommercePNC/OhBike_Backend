package com.example.OhBike.services;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.CouponResponse;
import com.example.OhBike.dto.response.ValidationResponse;

import java.util.List;
import java.util.UUID;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest request);
    CouponResponse updateCoupon(CouponRequest request, UUID id);
    CouponResponse deleteCoupon(UUID id);
    ValidationResponse validateCoupon(String code, Double purchaseAmount);
    List<CouponResponse> getAllCoupons();
    CouponResponse getByIdCoupon(UUID id);
    CouponResponse redeemCoupon(String code);
    CouponResponse cancelCoupon(String code);
}