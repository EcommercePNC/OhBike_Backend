package com.example.OhBike.controller;

import com.example.OhBike.dto.request.AddCartItemRequest;
import com.example.OhBike.dto.request.UpdateCartItemRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // GET /api/cart/items
    @GetMapping("/items")
    public ResponseEntity<GeneralResponse> getMyCart() {
        return buildResponse("Cart retrieved successfully", HttpStatus.OK, cartService.getMyCart());
    }

    // POST /api/cart/items
    @PostMapping("/items")
    public ResponseEntity<GeneralResponse> addItem(@Valid @RequestBody AddCartItemRequest request) {
        return buildResponse("Item added to cart", HttpStatus.OK, cartService.addItem(request));
    }

    // PUT /api/cart/items/{cartItemId}
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<GeneralResponse> updateItem(
            @PathVariable UUID cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return buildResponse("Cart item updated", HttpStatus.OK, cartService.updateItem(cartItemId, request));
    }

    // DELETE /api/cart/items/{cartItemId}
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<GeneralResponse> removeItem(@PathVariable UUID cartItemId) {
        return buildResponse("Item removed from cart", HttpStatus.OK, cartService.removeItem(cartItemId));
    }

    // DELETE /api/cart/items
    @DeleteMapping("/items")
    public ResponseEntity<GeneralResponse> clearCart() {
        return buildResponse("Cart cleared", HttpStatus.OK, cartService.clearCart());
    }

    // GET /api/cart/summary
    @GetMapping("/summary")
    public ResponseEntity<GeneralResponse> getSummary() {
        return buildResponse("Cart summary", HttpStatus.OK, cartService.getSummary());
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