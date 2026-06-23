package com.example.OhBike.service.impl;

import com.example.OhBike.common.mapper.OrderMapper;
import com.example.OhBike.dto.request.OrderDetailRequest;
import com.example.OhBike.dto.request.OrderRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.response.OrderResponse;
import com.example.OhBike.entity.*;
import com.example.OhBike.repository.*;
import com.example.OhBike.service.OrderService;
import com.example.OhBike.util.AuthUtil;
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
public class OrderServiceImpl implements OrderService {

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

    @Override
    @Transactional
    public GeneralResponse create(OrderRequest request) {
        UUID currentUserId = AuthUtil.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + currentUserId));

        ShippingMethod shippingMethod = shippingMethodRepository.findById(request.getShippingMethodId())
                .orElseThrow(() -> new NoSuchElementException("Shipping method not found with ID: " + request.getShippingMethodId()));

        BigDecimal shippingCost = shippingMethod.getBaseCost();

        Coupon coupon = null;
        BigDecimal discountTotal = BigDecimal.ZERO;

        if (request.getCouponId() != null) {
            coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new NoSuchElementException("Coupon not found with ID: " + request.getCouponId()));

            if (coupon.getDiscount() != null) {
                discountTotal = coupon.getDiscount().getValue();
            }
        }

        Order order = Order.builder()
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

        BigDecimal accumulatedSubtotal = BigDecimal.ZERO;
        List<OrderDetail> detailsList = new ArrayList<>();

        for (OrderDetailRequest itemRequest : request.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.getProductVariantId())
                    .orElseThrow(() -> new NoSuchElementException("Product variant not found with ID: " + itemRequest.getProductVariantId()));

            BigDecimal unitPrice = variant.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(itemRequest.getQuantity());
            BigDecimal detailSubtotal = unitPrice.multiply(quantity);

            accumulatedSubtotal = accumulatedSubtotal.add(detailSubtotal);

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .productVariant(variant)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(detailSubtotal)
                    .build();

            detailsList.add(detail);
        }

        order.setDetails(detailsList);
        order.setSubtotal(accumulatedSubtotal);

        BigDecimal finalTotal = accumulatedSubtotal.add(shippingCost).subtract(discountTotal);

        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            finalTotal = BigDecimal.ZERO;
        }

        order.setTotal(finalTotal);

        Order savedOrder = orderRepository.save(order);
        OrderResponse responseDto = orderMapper.toDto(savedOrder);

        return GeneralResponse.builder()
                .uri("/api/v1/orders")
                .message("Order registered and processed successfully")
                .status(201)
                .time(LocalDateTime.now())
                .data(responseDto)
                .build();
    }

    @Override
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