package com.example.OhBike.mapper;

import com.example.OhBike.dto.response.OrderDetailResponse;
import com.example.OhBike.dto.response.OrderResponse;
import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.OrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderDetailResponse toDetailDto(OrderDetail detail) {
        return OrderDetailResponse.builder()
                .orderDetailId(detail.getId())
                .variantId(detail.getVariant().getId())
                .productName(detail.getVariant().getProduct().getName())
                .size(detail.getVariant().getSize())
                .color(detail.getVariant().getColor())
                .sku(detail.getVariant().getSku())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getSubtotal())
                .build();
    }

    public OrderResponse toDto(Order order) {
        List<OrderDetailResponse> details = order.getDetails()
                .stream().map(this::toDetailDto).toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .shippingMethod(order.getShippingMethod().getName())
                .shippingCost(order.getShippingCost())
                .couponCode(order.getCoupon() != null ? order.getCoupon().getCode() : null)
                .subtotal(order.getSubtotal())
                .discountAmount(order.getDiscountAmount())
                .total(order.getTotal())
                .status(order.getStatus())
                .details(details)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}