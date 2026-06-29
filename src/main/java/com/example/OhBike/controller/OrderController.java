package com.example.OhBike.controller;

import com.example.OhBike.dto.request.CheckoutRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.OrderService;
import com.example.OhBike.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;

    // GET /api/orders/checkout/preview
    @GetMapping("/checkout/preview")
    public ResponseEntity<GeneralResponse> previewCheckout(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return buildResponse("Checkout preview", HttpStatus.OK,
                orderService.previewCheckout(request, email));
    }

    // POST /api/orders/checkout
    @PostMapping("/checkout")
    public ResponseEntity<GeneralResponse> checkout(
            @Valid @RequestBody CheckoutRequest request,
            Authentication authentication) {
        String email = authentication.getName();

        return buildResponse("Order created successfully", HttpStatus.CREATED,
                orderService.checkout(request, email));
    }

    // PATCH /api/orders/{orderId}/pay
    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<GeneralResponse> payOrder(@PathVariable UUID orderId, Authentication authentication) {
        String email = authentication.getName();

        return buildResponse("Order marked as paid", HttpStatus.OK,
                orderService.payOrder(orderId, email));
    }

    // PATCH /api/orders/{orderId}/ship
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SELLER')")
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
    public ResponseEntity<GeneralResponse> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        return buildResponse("User orders", HttpStatus.OK,
                orderService.getMyOrders(email));
    }

    // GET /api/orders
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<GeneralResponse> getAllOrders() {
        return buildResponse("All orders", HttpStatus.OK,
                orderService.getAllOrders());
    }

    // GET /api/orders/{orderId}/tracking
    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<GeneralResponse> getTracking(@PathVariable UUID orderId,
                                                       Authentication authentication) {
        String email = authentication.getName();
        return buildResponse("Order tracking", HttpStatus.OK,
                shipmentService.getTracking(email, orderId));
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