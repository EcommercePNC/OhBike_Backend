package com.example.OhBike.services.impl;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.CouponResponse;
import com.example.OhBike.entities.Coupon;
import com.example.OhBike.entities.Discount;
import com.example.OhBike.exceptions.BusinessRuleException;
import com.example.OhBike.exceptions.ResourceNotFoundException;
import com.example.OhBike.mapper.CouponMapper;
import com.example.OhBike.repositories.CouponRepository;
import com.example.OhBike.repositories.DiscountRepository;
import com.example.OhBike.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final DiscountRepository discountRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw new BusinessRuleException("El código de cupón ya existe: " + request.getCode());
        }

        Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado con ID: " + request.getDiscountId()));

        Coupon coupon = couponMapper.toEntityCreate(request, discount);
        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream()
                .map(couponMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public CouponResponse getByIdCoupon(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado con ID: " + id));
        return couponMapper.toDto(coupon);
    }

    @Override
    @Transactional
    public CouponResponse updateCoupon(CouponRequest request, UUID id) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado con ID: " + id));

        if (!existingCoupon.getCode().equalsIgnoreCase(request.getCode()) &&
                couponRepository.existsByCode(request.getCode())) {
            throw new BusinessRuleException("El código de cupón ya se encuentra en uso: " + request.getCode());
        }

        Coupon couponToUpdate = couponMapper.toEntityUpdate(request, existingCoupon);
        return couponMapper.toDto(couponRepository.save(couponToUpdate));
    }

    @Override
    @Transactional
    public CouponResponse deleteCoupon(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado con ID: " + id));

        if (coupon.getUsedCount() > 0) {
            throw new BusinessRuleException("No se puede eliminar un cupón que ya ha sido utilizado.");
        }

        CouponResponse response = couponMapper.toDto(coupon);
        couponRepository.deleteById(id);
        return response;
    }
}