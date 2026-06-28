package com.example.OhBike.service.impl;

import com.example.OhBike.dto.request.ShippingMethodRequest;
import com.example.OhBike.dto.response.ShippingMethodResponse;
import com.example.OhBike.entity.ShippingMethod;
import com.example.OhBike.exception.DuplicateFieldException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.ShippingMethodMapper;
import com.example.OhBike.repository.ShippingMethodRepository;
import com.example.OhBike.service.ShippingMethodService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {

    private final ShippingMethodRepository repository;
    private final ShippingMethodMapper mapper;

    @Override
    @Transactional
    public List<ShippingMethodResponse> getAllShippingMethods() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ShippingMethodResponse getShippingMethodById(UUID id) {
        ShippingMethod shippingMethod = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping method not found"));
        return mapper.toDto(shippingMethod);
    }

    @Override
    @Transactional
    public ShippingMethodResponse createShippingMethod(ShippingMethodRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new DuplicateFieldException("shippingMethod", "shipping method already exists");
        }
        ShippingMethod entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public ShippingMethodResponse updateShippingMethod(UUID id, ShippingMethodRequest request) {
        ShippingMethod shippingMethod = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("shipping method not found"));

        if (!shippingMethod.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            throw new DuplicateFieldException("name", "shipping method already exists");
        }
        shippingMethod.setName(request.getName());
        shippingMethod.setBaseCost(request.getBaseCost());

        return mapper.toDto(repository.save(shippingMethod));
    }

    @Override
    @Transactional
    public void deleteShippingMethod(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("shipping method not found");
        }
        repository.deleteById(id);
    }

}

