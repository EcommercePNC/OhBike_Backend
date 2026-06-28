package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.response.CheckoutValidationResponse;
import com.example.OhBike.entity.Cart;
import com.example.OhBike.entity.CartItem;
import com.example.OhBike.entity.Coupon;
import com.example.OhBike.entity.ShippingMethod;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.CartRepository;
import com.example.OhBike.repository.CouponRepository;
import com.example.OhBike.repository.ShippingMethodRepository;
import com.example.OhBike.service.CheckoutValidationService;
import com.example.OhBike.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutValidationServiceImpl implements CheckoutValidationService {

    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final ShippingMethodRepository shippingMethodRepository;

    @Override
    public CheckoutValidationResponse validate(CheckoutRequest request) {
        List<String> errors = new ArrayList<>();

        UUID userId = AuthUtil.getCurrentUserId();
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElse(null);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            errors.add("Cart is empty. Add items before checkout.");
            return CheckoutValidationResponse.builder()
                    .valid(false)
                    .errors(errors)
                    .build();
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            int available = item.getVariant().getStock();
            int requested = item.getQuantity();

            if (!item.getVariant().getActive()) {
                errors.add("Variant '" + item.getVariant().getSku() + "' is no longer available.");
            } else if (available < requested) {
                errors.add("Insufficient stock for '" + item.getVariant().getSku()
                        + "'. Requested: " + requested + ", available: " + available + ".");
            } else {
                subtotal = subtotal.add(
                        item.getVariant().getPrice()
                                .multiply(BigDecimal.valueOf(requested))
                );
            }
        }

        ShippingMethod shipping = null;
        if (request.getShippingMethodId() == null) {
            errors.add("Shipping method is required.");
        } else {
            shipping = shippingMethodRepository.findById(request.getShippingMethodId())
                    .orElse(null);
            if (shipping == null) {
                errors.add("Shipping method not found: " + request.getShippingMethodId());
            }
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        String couponCode = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            Coupon coupon = couponRepository.findByCode(request.getCouponCode()).orElse(null);

            if (coupon == null) {
                errors.add("Coupon not found: '" + request.getCouponCode() + "'.");
            } else if (coupon.getExpirationDate().isBefore(LocalDate.now())) {
                errors.add("Coupon '" + request.getCouponCode() + "' has expired.");
            } else if (coupon.getUsedCount() >= coupon.getMaxUses()) {
                errors.add("Coupon '" + request.getCouponCode() + "' has no remaining uses.");
            } else {
                couponCode = coupon.getCode();
                discountAmount = calculateDiscount(subtotal, coupon);
            }
        }

        if (!errors.isEmpty()) {
            return CheckoutValidationResponse.builder()
                    .valid(false)
                    .errors(errors)
                    .build();
        }

        BigDecimal shippingCost = shipping.getBaseCost();
        BigDecimal total = subtotal.subtract(discountAmount).add(shippingCost);

        return CheckoutValidationResponse.builder()
                .valid(true)
                .errors(List.of())
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .couponCode(couponCode)
                .shippingCost(shippingCost)
                .shippingMethod(shipping.getName())
                .total(total)
                .build();
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal, Coupon coupon) {
        BigDecimal value = coupon.getDiscount().getValue();

        return switch (coupon.getDiscount().getDiscountType()) {
            case PERCENTAGE -> {
                BigDecimal factor = value.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                yield subtotal.multiply(factor).setScale(2, RoundingMode.HALF_UP);
            }
            case FIXED_AMOUNT -> value.min(subtotal); // nunca descuento mayor al subtotal
            case NONE -> BigDecimal.ZERO;
        };
    }
}