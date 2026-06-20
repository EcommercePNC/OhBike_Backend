package com.example.OhBike.controller;

import jakarta.validation.Valid;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.service.ShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipping-methods")
@CrossOrigin(origins = "*")
public class ShippingMethodController {

    @Autowired
    private ShippingMethodService shippingMethodService;

    @PostMapping
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody ShippingMethodRequest request) {
        return new ResponseEntity<>(shippingMethodService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAll() {
        return new ResponseEntity<>(shippingMethodService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        return new ResponseEntity<>(shippingMethodService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> update(@PathVariable UUID id, @Valid @RequestBody ShippingMethodRequest request) {
        return new ResponseEntity<>(shippingMethodService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(shippingMethodService.delete(id), HttpStatus.OK);
    }
}
