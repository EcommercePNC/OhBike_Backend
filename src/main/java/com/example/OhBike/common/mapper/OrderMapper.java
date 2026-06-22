package com.example.OhBike.common.mapper;

import com.example.OhBike.dto.response.OrderResponse;
import com.example.OhBike.dto.response.OrderDetailResponse;
import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.OrderDetail;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderMapper {

    public OrderResponse toDto(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        if (order.getDetails() != null) {
            detailResponses = order.getDetails().stream()
                    .map(this::toOrderDetailDto)
                    .toList();
        }

        String couponCode = (order.getCoupon() != null) ? order.getCoupon().getCode() : "None";
        String userName = (order.getUser() != null) ? order.getUser().getName() : "Anonymous";

        return new OrderResponse(
                order.getId(),
                order.getUser() != null ? order.getUser().getId() : null,
                userName,
                couponCode,
                order.getShippingMethod() != null ? order.getShippingMethod().getId() : null,
                order.getOrderDate(),
                order.getSubtotal(),
                order.getShippingCost(),
                order.getDiscountTotal(),
                order.getStatus(),
                order.getTotal(),
                detailResponses
        );
    }

    private OrderDetailResponse toOrderDetailDto(OrderDetail detail) {
        if (detail == null) {
            return null;
        }

        String productName = "Unknown Product";
        java.util.UUID variantId = null;

        if (detail.getProductVariant() != null) {
            variantId = detail.getProductVariant().getId();
            if (detail.getProductVariant().getProduct() != null) {
                productName = detail.getProductVariant().getProduct().getName();
            }
        }

        return new OrderDetailResponse(
                detail.getId(),
                variantId,
                productName,
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubtotal()
        );
    }
}
