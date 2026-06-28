package com.example.OhBike.service.impl;

import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.dto.request.PaymentMethodRequest;
import com.example.OhBike.dto.response.PaymentMethodResponse;
import com.example.OhBike.dto.response.ProductResponse;
import com.example.OhBike.exception.DuplicateFieldException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.PaymentMethodMapper;
import com.example.OhBike.entity.PaymentMethod;
import com.example.OhBike.repository.PaymentMethodRepository;
import com.example.OhBike.service.PaymentMethodService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repository;
    private final PaymentMethodMapper mapper;

    @Override
    @Transactional
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentMethodResponse getPaymentMethodById(UUID id) {
        PaymentMethod paymentMethod = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
        return mapper.toDto(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new DuplicateFieldException("paymentMethod", "payment method already exists");
        }
        PaymentMethod entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public PaymentMethodResponse updatePaymentMethod(UUID id, PaymentMethodRequest request) {
        PaymentMethod paymentMethod = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        if (!paymentMethod.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            throw new DuplicateFieldException("name", "payment method already exists");
        }
        paymentMethod.setName(request.getName());
        paymentMethod.setDescription(request.getDescription());

        return mapper.toDto(repository.save(paymentMethod));
    }

    @Override
    @Transactional
    public void deletePaymentMethod(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("payment method not found");
        }
        repository.deleteById(id);
    }

}

