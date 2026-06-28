package com.example.OhBike.controller;

import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;
import com.example.OhBike.service.ShippingMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipping-methods")
@RequiredArgsConstructor
public class ShippingMethodController {

    private final ShippingMethodService service;

    @PostMapping
    public ResponseEntity<ShippingMethodResponse> create(@Valid @RequestBody ShippingMethodRequest request) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.createShippingMethod(request));
    }

    @GetMapping
    public ResponseEntity<List<ShippingMethodResponse>> getAll() {
        return ResponseEntity.ok(service.getAllShippingMethods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingMethodResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getShippingMethodById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingMethodResponse> update(@PathVariable UUID id, @Valid @RequestBody ShippingMethodRequest request) {
        return ResponseEntity.ok(service.updateShippingMethod(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteShippingMethod(id);
        return ResponseEntity.noContent().build();
    }
}
