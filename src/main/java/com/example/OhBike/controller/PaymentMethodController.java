package com.example.OhBike.controller;

import com.example.OhBike.dto.response.PaymentMethodResponse;
import jakarta.validation.Valid;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.PaymentMethodRequest;
import com.example.OhBike.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService service;

    @PostMapping
    public ResponseEntity<PaymentMethodResponse> create(@Valid @RequestBody PaymentMethodRequest request) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.createPaymentMethod(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getAll() {
        return ResponseEntity.ok(service.getAllPaymentMethods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getPaymentMethodById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> update(@PathVariable UUID id, @Valid @RequestBody PaymentMethodRequest request) {
        return ResponseEntity.ok(service.updatePaymentMethod(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}
