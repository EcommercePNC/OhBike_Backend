package com.example.OhBike.service;

import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;
import com.example.OhBike.entities.ShippingMethod;
import com.example.OhBike.repository.ShippingMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ShippingMethodService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    public GeneralResponse create(ShippingMethodRequest request) {
        ShippingMethod entity = ShippingMethod.builder()
                .name(request.name())
                .baseCost(request.baseCost())
                .build();
        ShippingMethod saved = shippingMethodRepository.save(entity);
        return new GeneralResponse("Shipping method registered successfully", convertToResponse(saved));
    }

    public GeneralResponse getAll() {
        List<ShippingMethodResponse> list = shippingMethodRepository.findAll().stream()
                .map(this::convertToResponse).toList();
        return new GeneralResponse("Shipping methods retrieved successfully", list);
    }

    public GeneralResponse getById(UUID id) {
        ShippingMethod entity = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipping method not found with ID: " + id));
        return new GeneralResponse("Shipping method found", convertToResponse(entity));
    }

    public GeneralResponse update(UUID id, ShippingMethodRequest request) {
        ShippingMethod entity = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipping method not found with ID: " + id));
        entity.setName(request.name());
        entity.setBaseCost(request.baseCost());
        ShippingMethod updated = shippingMethodRepository.save(entity);
        return new GeneralResponse("Shipping method updated successfully", convertToResponse(updated));
    }

    public GeneralResponse delete(UUID id) {
        ShippingMethod entity = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shipping method not found with ID: " + id));
        shippingMethodRepository.delete(entity);
        return new GeneralResponse("Shipping method deleted successfully", null);
    }

    private ShippingMethodResponse convertToResponse(ShippingMethod entity) {
        return new ShippingMethodResponse(entity.getId(), entity.getName(), entity.getBaseCost());
    }
}
