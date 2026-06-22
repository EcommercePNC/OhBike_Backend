package com.example.OhBike.service;

import com.example.OhBike.common.mapper.OrderMapper;
import com.example.OhBike.dto.request.OrderRequest;
import com.example.OhBike.dto.request.OrderDetailRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.response.OrderResponse;
import com.example.OhBike.entity.*;
import com.example.OhBike.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public GeneralResponse create(OrderRequest request) {
        // 1. Validate User
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + request.userId()));

        // 2. Validate Shipping Method
        ShippingMethod shippingMethod = shippingMethodRepository.findById(request.shippingMethodId())
                .orElseThrow(() -> new NoSuchElementException("Shipping method not found with ID: " + request.shippingMethodId()));
        BigDecimal shippingCost = shippingMethod.getBaseCost();

        // 3. Validate Coupon
        Coupon coupon = null;
        BigDecimal discountTotal = BigDecimal.ZERO;
        if (request.couponId() != null) {
            coupon = couponRepository.findById(request.couponId())
                    .orElseThrow(() -> new NoSuchElementException("Coupon not found with ID: " + request.couponId()));

            if (coupon.getDiscount() != null) {
                discountTotal = coupon.getDiscount().getValue();
            }
        }



        // 4. Initialize Order
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .user(user)
                .coupon(coupon)
                .shippingMethod(shippingMethod)
                .shippingCost(shippingCost)
                .discountTotal(discountTotal)
                .status("PENDING")
                .subtotal(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .orderDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .details(new ArrayList<>())
                .build();

        // 5. Process Cart Items (Details)
        BigDecimal accumulatedSubtotal = BigDecimal.ZERO;
        List<OrderDetail> detailsList = new ArrayList<>();

        for (OrderDetailRequest itemRequest : request.items()) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.productVariantId())
                    .orElseThrow(() -> new NoSuchElementException("Product variant not found with ID: " + itemRequest.productVariantId()));

            // Validate inventory stock
            if (variant.getStock() < itemRequest.quantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + variant.getProduct().getName());
            }

            // Deduct inventory stock
            variant.setStock(variant.getStock() - itemRequest.quantity());
            productVariantRepository.save(variant);

            BigDecimal unitPrice = variant.getPrice();
            BigDecimal qtyBigDecimal = BigDecimal.valueOf(itemRequest.quantity().longValue());
            BigDecimal detailSubtotal = unitPrice.multiply(qtyBigDecimal);

            accumulatedSubtotal = accumulatedSubtotal.add(detailSubtotal);

            OrderDetail detail = OrderDetail.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .productVariant(variant)
                    .quantity(itemRequest.quantity())
                    .unitPrice(unitPrice)
                    .subtotal(detailSubtotal)
                    .build();

            detailsList.add(detail);
        }

        // 6. Set Final Totals
        order.setDetails(detailsList);
        order.setSubtotal(accumulatedSubtotal);

        BigDecimal finalTotal = accumulatedSubtotal.add(shippingCost).subtract(discountTotal);
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            finalTotal = BigDecimal.ZERO;
        }
        order.setTotal(finalTotal);

        // 7. Save to Database
        Order savedOrder = orderRepository.save(order);

        // 8. Map to Response DTO
        OrderResponse responseDto = orderMapper.toDto(savedOrder);

        return GeneralResponse.builder()
                .uri("/api/v1/orders")
                .message("Order registered and processed successfully")
                .status(201)
                .time(LocalDateTime.now())
                .data(responseDto)
                .build();
    }

    @Transactional(readOnly = true)
    public GeneralResponse getById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));

        OrderResponse responseDto = orderMapper.toDto(order);

        return GeneralResponse.builder()
                .uri("/api/v1/orders/" + id)
                .message("Order retrieved successfully")
                .status(200)
                .time(LocalDateTime.now())
                .data(responseDto)
                .build();
    }
}
