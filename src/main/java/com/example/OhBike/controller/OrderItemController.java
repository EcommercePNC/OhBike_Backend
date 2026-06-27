package com.example.OhBike.controller;

import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/vendor/orders/items")
    public ResponseEntity<GeneralResponse> getVendorOrderItems() {
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .uri("/api/vendor/orders/items")
                        .message("Vendor sold items retrieved successfully")
                        .status(200)
                        .time(LocalDateTime.now())
                        .data(orderItemService.getVendorOrderItems())
                        .build()
        );
    }

    @GetMapping("/admin/orders/items")
    public ResponseEntity<GeneralResponse> getAdminOrderItems() {
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .uri("/api/admin/orders/items")
                        .message("Admin sold items retrieved successfully")
                        .status(200)
                        .time(LocalDateTime.now())
                        .data(orderItemService.getAdminOrderItems())
                        .build()
        );
    }
}