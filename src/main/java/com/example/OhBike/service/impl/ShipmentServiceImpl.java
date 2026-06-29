package com.example.OhBike.service.impl;

import com.example.OhBike.dto.response.OrderTrackingResponse;
import com.example.OhBike.entity.Order;
import com.example.OhBike.entity.User;
import com.example.OhBike.entity.enums.OrderStatus;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.repository.OrderRepository;
import com.example.OhBike.repository.UserRepository;
import com.example.OhBike.service.ShipmentService;
import com.example.OhBike.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderTrackingResponse getTracking(String email, UUID orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));

        boolean isOwner = user.getId().equals(order.getUser().getId());
        boolean isAdmin = user.getRole().getName().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new BusinessRuleException("ACCESS_DENIED",
                    "You are not allowed to track this order.");
        }

        // Genera guía simulada solo si ya fue enviada
        String guide = null;
        String carrier = null;

        if (order.getStatus() == OrderStatus.SHIPPED) {
            guide = "OB-" + orderId.toString().substring(0, 8).toUpperCase();
            carrier = order.getShippingMethod().getName();
        }

        return OrderTrackingResponse.builder()
                .status(order.getStatus().name())
                .guide(guide)
                .carrier(carrier)
                .build();
    }
}