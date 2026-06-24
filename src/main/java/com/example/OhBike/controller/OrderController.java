package com.example.OhBike.controller;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // GET /api/orders/checkout/preview
    @GetMapping("/checkout/preview")
    public ResponseEntity<GeneralResponse> previewCheckout(
            @Valid @RequestBody CheckoutRequest request) {
        return buildResponse("Checkout preview", HttpStatus.OK,
                orderService.previewCheckout(request));
    }

    // POST /api/orders/checkout
    @PostMapping("/checkout")
    public ResponseEntity<GeneralResponse> checkout(
            @Valid @RequestBody CheckoutRequest request) {
        return buildResponse("Order created successfully", HttpStatus.CREATED,
                orderService.checkout(request));
    }

    // PATCH /api/orders/{orderId}/pay
    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<GeneralResponse> payOrder(@PathVariable UUID orderId) {
        return buildResponse("Order marked as paid", HttpStatus.OK,
                orderService.payOrder(orderId));
    }

    // PATCH /api/orders/{orderId}/ship
    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<GeneralResponse> shipOrder(@PathVariable UUID orderId) {
        return buildResponse("Order marked as shipped", HttpStatus.OK,
                orderService.shipOrder(orderId));
    }

    // GET /api/orders/{orderId}
    @GetMapping("/{orderId}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID orderId) {
        return buildResponse("Order found", HttpStatus.OK,
                orderService.getById(orderId));
    }

    // GET /api/orders/my
    @GetMapping("/my")
    public ResponseEntity<GeneralResponse> getMyOrders() {
        return buildResponse("User orders", HttpStatus.OK,
                orderService.getMyOrders());
    }

    // GET /api/orders
    @GetMapping
    public ResponseEntity<GeneralResponse> getAllOrders() {
        return buildResponse("All orders", HttpStatus.OK,
                orderService.getAllOrders());
    }

    private ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
        return ResponseEntity.status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}