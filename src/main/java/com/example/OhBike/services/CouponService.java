package com.example.OhBike.services;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.CouponResponse;
import java.util.List;
import java.util.UUID;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest request);
    CouponResponse updateCoupon(CouponRequest request, UUID id);
    CouponResponse deleteCoupon(UUID id);
    List<CouponResponse> getAllCoupons();
    CouponResponse getByIdCoupon(UUID id);
}