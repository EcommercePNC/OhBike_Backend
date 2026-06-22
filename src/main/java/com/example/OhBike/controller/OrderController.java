package com.example.OhBike.controller;

import jakarta.validation.Valid;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.OrderRequest;
import com.example.OhBike.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody OrderRequest request) {
        GeneralResponse response = orderService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        GeneralResponse response = orderService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
