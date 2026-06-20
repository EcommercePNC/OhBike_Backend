package com.example.OhBike.controller;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.services.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody DiscountRequest request) {
        return buildResponse("Descuento creado", HttpStatus.CREATED, discountService.createDiscount(request));
    }

    @GetMapping("/active")
    public ResponseEntity<GeneralResponse> findAllActive() {
        return buildResponse("Descuentos activos encontrados", HttpStatus.OK, discountService.findAllActiveDiscount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getById(@PathVariable UUID id) {
        return buildResponse("Descuento encontrado", HttpStatus.OK, discountService.getByIdDiscount(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> update(@PathVariable UUID id, @Valid @RequestBody DiscountRequest request) {
        return buildResponse("Descuento se ha actualizado exitosamente", HttpStatus.OK, discountService.updateDiscount(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@PathVariable UUID id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Descuento eliminado exitosamente.")
                .status(HttpStatus.OK.value())
                .time(LocalDateTime.now())
                .build());
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllActiveDiscounts() {
        return buildResponse("Lista de descuentos activos", HttpStatus.OK, discountService.findAllActiveDiscount());
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