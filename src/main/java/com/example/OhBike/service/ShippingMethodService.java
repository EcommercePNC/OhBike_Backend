package com.example.OhBike.service;

import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;
import com.example.OhBike.common.mapper.ShippingMethodMapper;
import com.example.OhBike.entity.ShippingMethod;
import com.example.OhBike.repository.ShippingMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ShippingMethodService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Autowired
    private ShippingMethodMapper shippingMethodMapper;

    public GeneralResponse create(ShippingMethodRequest request) {
        ShippingMethod entity = shippingMethodMapper.toEntity(request);
        ShippingMethod saved = shippingMethodRepository.save(entity);

        return GeneralResponse.builder()
                .uri("/api/v1/shipping-methods")
                .message("Método de envío registrado exitosamente")
                .status(201)
                .time(LocalDateTime.now())
                .data(shippingMethodMapper.toDto(saved))
                .build();
    }

    public GeneralResponse getAll() {
        List<ShippingMethodResponse> list = shippingMethodRepository.findAll().stream()
                .map(shippingMethodMapper::toDto)
                .toList();

        return GeneralResponse.builder()
                .uri("/api/v1/shipping-methods")
                .message("Métodos de envío recuperados exitosamente")
                .status(200)
                .time(LocalDateTime.now())
                .data(list)
                .build();
    }

    public GeneralResponse getById(UUID id) {
        ShippingMethod entity = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de envío no encontrado con el ID: " + id));

        return GeneralResponse.builder()
                .uri("/api/v1/shipping-methods/" + id)
                .message("Método de envío encontrado")
                .status(200)
                .time(LocalDateTime.now())
                .data(shippingMethodMapper.toDto(entity))
                .build();
    }

    public GeneralResponse update(UUID id, ShippingMethodRequest request) {
        ShippingMethod entity = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de envío no encontrado con el ID: " + id));

        entity.setName(request.getName());
        entity.setBaseCost(request.getBaseCost());
        ShippingMethod updated = shippingMethodRepository.save(entity);

        return GeneralResponse.builder()
                .uri("/api/v1/shipping-methods/" + id)
                .message("Método de envío actualizado exitosamente")
                .status(200)
                .time(LocalDateTime.now())
                .data(shippingMethodMapper.toDto(updated))
                .build();
    }

    public GeneralResponse delete(UUID id) {
        ShippingMethod entity = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Método de envío no encontrado con el ID: " + id));
        shippingMethodRepository.delete(entity);

        return GeneralResponse.builder()
                .uri("/api/v1/shipping-methods/" + id)
                .message("Método de envío eliminado exitosamente")
                .status(200)
                .time(LocalDateTime.now())
                .data(null)
                .build();
    }
}

