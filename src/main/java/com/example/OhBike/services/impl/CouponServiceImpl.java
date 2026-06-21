package com.example.OhBike.services.impl;

import com.example.OhBike.dto.request.CouponRequest;
import com.example.OhBike.dto.response.CouponResponse;
import com.example.OhBike.dto.response.ValidationResponse;
import com.example.OhBike.entity.Coupon;
import com.example.OhBike.entity.Discount;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.CouponMapper;
import com.example.OhBike.repository.CouponRepository;
import com.example.OhBike.repository.DiscountRepository;
import com.example.OhBike.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
        if (request.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("La fecha de expiración no puede ser una fecha pasada");
        }
        Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado"));

        if (!discount.getActive()) {
            throw new BusinessRuleException("No puedes crear un cupón para un descuento inactivo");
        }

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

    @Override
    @Transactional(readOnly = true)
    public ValidationResponse validateCoupon(String code, Double purchaseAmount) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado con código: " + code));

        if (coupon.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("El cupón ha expirado");
        }

        if (coupon.getUsedCount() >= coupon.getMaxUses()) {
            throw new BusinessRuleException("Cupón esta agotado");
        }

        return ValidationResponse.builder()
                .code(coupon.getCode())
                .isValid(true)
                .message("Cupón válido")
                .discountValue(coupon.getDiscount().getValue().doubleValue())
                .discountType(coupon.getDiscount().getDiscountType().name())
                .build();
    }

    @Override
    @Transactional
    public CouponResponse redeemCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));

        if (coupon.getUsedCount() >= coupon.getMaxUses()) {
            throw new BusinessRuleException("Cupón agotado");
        }

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        return couponMapper.toDto(couponRepository.save(coupon));
    }

    @Override
    @Transactional
    public CouponResponse cancelCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));

        if (coupon.getUsedCount() > 0) {
            coupon.setUsedCount(coupon.getUsedCount() - 1);
        }

        return couponMapper.toDto(couponRepository.save(coupon));
    }

    public BigDecimal applyDiscount(BigDecimal originalPrice, Coupon coupon) {
        Discount discount = coupon.getDiscount();
        BigDecimal discountValue = BigDecimal.valueOf(discount.getValue().doubleValue());

        return switch (discount.getDiscountType()) {
            case PERCENTAGE -> {
                BigDecimal percentage = discountValue.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                yield originalPrice.subtract(originalPrice.multiply(percentage));
            }
            case FIXED_AMOUNT -> {
                yield originalPrice.subtract(discountValue);
            }
            case NONE -> {
                yield originalPrice;
            }
        };
    }
}
