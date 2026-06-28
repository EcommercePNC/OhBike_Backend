package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.response.CartItemResponse;
import com.example.OhBike.dto.response.CheckoutSummaryResponse;
import com.example.OhBike.dto.response.CheckoutValidationResponse;
import com.example.OhBike.dto.response.OrderResponse;
import com.example.OhBike.entity.*;
import com.example.OhBike.entity.enums.OrderStatus;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.CartMapper;
import com.example.OhBike.mapper.OrderMapper;
import com.example.OhBike.repository.*;
import com.example.OhBike.service.CheckoutValidationService;
import com.example.OhBike.service.OrderService;
import com.example.OhBike.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final CouponRepository couponRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final CouponServiceImpl couponService;
    private final CheckoutValidationService checkoutValidationService;

    @Override
    public CheckoutSummaryResponse previewCheckout(CheckoutRequest request, String email) {
        Cart cart = getCartOfCurrentUser(email);
        validateCartNotEmpty(cart);

        findPayment(request.getPaymentMethodId());
        ShippingMethod shipping = findShipping(request.getShippingMethodId());

        BigDecimal subtotal = calculateSubtotal(cart);
        BigDecimal discountAmount = BigDecimal.ZERO;
        String couponCode = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            Coupon coupon = findValidCoupon(request.getCouponCode());
            BigDecimal afterDiscount = couponService.applyDiscount(subtotal, coupon);
            discountAmount = subtotal.subtract(afterDiscount);
            couponCode = coupon.getCode();
        }

        BigDecimal shippingCost = shipping.getBaseCost();
        BigDecimal total = subtotal.subtract(discountAmount).add(shippingCost);

        List<CartItemResponse> itemDtos = cart.getItems()
                .stream()
                .map(cartMapper::toItemDto)
                .toList();

        return CheckoutSummaryResponse.builder()
                .items(itemDtos)
                .subtotal(subtotal)
                .couponCode(couponCode)
                .discountAmount(discountAmount)
                .shippingMethod(shipping.getName())
                .shippingCost(shippingCost)
                .total(total)
                .build();
    }

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request, String email) {

        CheckoutValidationResponse validation = checkoutValidationService.validate(request, email);
        if (!validation.isValid()) {
            String reasons = String.join(" | ", validation.getErrors());
            throw new BusinessRuleException("CHECKOUT_INVALID",
                    "Checkout failed validation: " + reasons);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = getCartOfCurrentUser(email);
        PaymentMethod paymentMethod = findPayment(request.getPaymentMethodId());
        ShippingMethod shipping = findShipping(request.getShippingMethodId());

        BigDecimal subtotal = calculateSubtotal(cart);
        BigDecimal discountAmount = BigDecimal.ZERO;
        Coupon coupon = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            coupon = findValidCoupon(request.getCouponCode());
            BigDecimal afterDiscount = couponService.applyDiscount(subtotal, coupon);
            discountAmount = subtotal.subtract(afterDiscount);
        }

        BigDecimal shippingCost = shipping.getBaseCost();
        BigDecimal total = subtotal.subtract(discountAmount).add(shippingCost);

        Order order = Order.builder()
                .user(user)
                .coupon(coupon)
                .paymentMethod(paymentMethod)
                .shippingMethod(shipping)
                .status(OrderStatus.PENDING)
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .shippingCost(shippingCost)
                .total(total)
                .build();

        List<OrderDetail> details = cart.getItems().stream()
                .map(item -> {
                    ProductVariant variant = item.getVariant();
                    variant.setStock(variant.getStock() - item.getQuantity());
                    variantRepository.save(variant);

                    return OrderDetail.builder()
                            .order(order)
                            .variant(variant)
                            .quantity(item.getQuantity())
                            .unitPrice(variant.getPrice())
                            .subtotal(variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .build();
                })
                .toList();

        order.setDetails(details);
        Order saved = orderRepository.save(order);

        if (coupon != null) {
            couponService.redeemCoupon(coupon.getCode());
        }

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderResponse payOrder(UUID orderId, String email) {
        Order order = findOrderOfCurrentUser(orderId, email);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("ORDER_INVALID_STATUS",
                    "Order cannot be paid. Current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.PAID);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse shipOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessRuleException("ORDER_INVALID_STATUS",
                    "Order cannot be shipped. Current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.SHIPPED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderResponse getById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));;
        return orderRepository.findByUser_Id(user.getId())
                .stream().map(orderMapper::toDto).toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream().map(orderMapper::toDto).toList();
    }


    private Cart getCartOfCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));;
        return cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + email));
    }

    private void validateCartNotEmpty(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BusinessRuleException("CART_EMPTY", "Cannot checkout with an empty cart.");
        }
    }

    private PaymentMethod findPayment(UUID paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found: " + paymentMethodId));
    }

    private ShippingMethod findShipping(UUID shippingMethodId) {
        return shippingMethodRepository.findById(shippingMethodId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping method not found: " + shippingMethodId));
    }

    private Coupon findValidCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found: " + code));

        if (coupon.getExpirationDate().isBefore(java.time.LocalDate.now())) {
            throw new BusinessRuleException("COUPON_EXPIRED", "Coupon has expired: " + code);
        }

        if (coupon.getUsedCount() >= coupon.getMaxUses()) {
            throw new BusinessRuleException("COUPON_EXHAUSTED", "Coupon has no remaining uses: " + code);
        }

        return coupon;
    }

    private BigDecimal calculateSubtotal(Cart cart) {
        return cart.getItems().stream()
                .map(item -> item.getVariant().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order findOrderOfCurrentUser(UUID orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUser().getEmail().equals(email)) {
            throw new BusinessRuleException("ACCESS_DENIED",
                    "This order does not belong to the current user.");
        }
        return order;
    }
}