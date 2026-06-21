package com.example.OhBike.services.impl;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.DiscountResponse;
import com.example.OhBike.entity.Discount;
import com.example.OhBike.exception.BusinessRuleException;
import com.example.OhBike.exception.ResourceNotFoundException;
import com.example.OhBike.mapper.DiscountMapper;
import com.example.OhBike.repository.DiscountRepository;
import com.example.OhBike.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    @Override
    @Transactional
    public DiscountResponse createDiscount(DiscountRequest request) {
        validateDates(request);
        Discount entity = discountMapper.toEntity(request);
        Discount saved = discountRepository.save(entity);
        return discountMapper.toDto(saved);
    }

    @Override
    @Transactional
    public DiscountResponse updateDiscount(UUID id, DiscountRequest request) {
        validateDates(request);
        Discount entity = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado con ID: " + id));
        discountMapper.updateEntity(entity, request);
        Discount updated = discountRepository.save(entity);

        return discountMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteDiscount(UUID id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado"));
        discount.setActive(false);
        discountRepository.save(discount);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountResponse getByIdDiscount(UUID id) {
        return discountRepository.findById(id)
                .map(discountMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscountResponse> findAllActiveDiscount() {
        return discountRepository.findByActiveTrue().stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateDates(DiscountRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessRuleException("La fecha de inicio no puede ser posterior a la que finalice");
        }
    }
}