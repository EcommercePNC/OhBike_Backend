package com.example.OhBike.service;

import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.PaymentMethodRequest;
import com.example.OhBike.dto.response.PaymentMethodResponse;
import com.example.OhBike.mapper.PaymentMethodMapper;
import com.example.OhBike.entity.PaymentMethod;
import com.example.OhBike.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    public GeneralResponse create(PaymentMethodRequest request) {
        PaymentMethod entity = paymentMethodMapper.toEntity(request);
        PaymentMethod saved = paymentMethodRepository.save(entity);

        return GeneralResponse.builder()
                .uri("/api/v1/payment-methods")
                .message("Método de pago registrado exitosamente")
                .status(201)
                .time(LocalDateTime.now())
                .data(paymentMethodMapper.toDto(saved))
                .build();
    }

    public GeneralResponse getAll() {
        List<PaymentMethodResponse> list = paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::toDto)
                .toList();

        return GeneralResponse.builder()
                .uri("/api/v1/payment-methods")
                .message("Métodos de pago recuperados exitosamente")
                .status(200)
                .time(LocalDateTime.now())
                .data(list)
                .build();
    }

    public GeneralResponse getById(UUID id) {
        PaymentMethod entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de pago no encontrado con el ID: " + id));

        return GeneralResponse.builder()
                .uri("/api/v1/payment-methods/" + id)
                .message("Método de pago encontrado")
                .status(200)
                .time(LocalDateTime.now())
                .data(paymentMethodMapper.toDto(entity))
                .build();
    }

    public GeneralResponse update(UUID id, PaymentMethodRequest request) {
        PaymentMethod entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de pago no encontrado con el ID: " + id));

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        PaymentMethod updated = paymentMethodRepository.save(entity);

        return GeneralResponse.builder()
                .uri("/api/v1/payment-methods/" + id)
                .message("Método de pago actualizado exitosamente")
                .status(200)
                .time(LocalDateTime.now())
                .data(paymentMethodMapper.toDto(updated))
                .build();
    }

    public GeneralResponse delete(UUID id) {
        PaymentMethod entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de pago no encontrado con el ID: " + id));
        paymentMethodRepository.delete(entity);

        return GeneralResponse.builder()
                .uri("/api/v1/payment-methods/" + id)
                .message("Método de pago eliminado exitosamente")
                .status(200)
                .time(LocalDateTime.now())
                .data(null)
                .build();
    }
}

