package com.example.OhBike.controllers;

import jakarta.validation.Valid;
import com.example.OhBike.dtos.GeneralResponse;
import com.example.OhBike.dtos.request.PaymentMethodRequest;
import com.example.OhBike.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-methods")
@CrossOrigin(origins = "*")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody PaymentMethodRequest request) {
        // Ejecutamos el servicio y guardamos la respuesta del record
        GeneralResponse response = paymentMethodService.create(request);
        // Retornamos el objeto response explícitamente dentro de la entidad HTTP
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        GeneralResponse response = paymentMethodService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        GeneralResponse response = paymentMethodService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> update(@PathVariable UUID id, @Valid @RequestBody PaymentMethodRequest request) {
        GeneralResponse response = paymentMethodService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@PathVariable UUID id) {
        GeneralResponse response = paymentMethodService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
