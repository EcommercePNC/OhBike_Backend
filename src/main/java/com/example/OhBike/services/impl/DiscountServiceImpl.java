package com.example.OhBike.services.impl;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.DiscountResponse;
import com.example.OhBike.dto.response.GeneralResponse;
import com.example.OhBike.entities.Discount;
import com.example.OhBike.exceptions.BusinessRuleException;
import com.example.OhBike.exceptions.ResourceNotFoundException;
import com.example.OhBike.mapper.DiscountMapper;
import com.example.OhBike.repositories.DiscountRepository;
import com.example.OhBike.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    @Override
    @Transactional
    public GeneralResponse createDiscount(DiscountRequest request) {
        validateDates(request);
        Discount entity = discountMapper.toEntity(request);
        Discount saved = discountRepository.save(entity);

        return GeneralResponse.builder()
                .message("Descuento creado exitosamente.")
                .data(discountMapper.toDto(saved))
                .build();
    }

    @Override
    @Transactional
    public GeneralResponse updateDiscount(UUID id, DiscountRequest request) {
        validateDates(request);
        Discount entity = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado con ID: " + id));

        discountMapper.updateEntity(entity, request);
        Discount updated = discountRepository.save(entity);

        return GeneralResponse.builder()
                .message("Descuento actualizado exitosamente.")
                .data(discountMapper.toDto(updated))
                .build();
    }

    @Override
    @Transactional
    public GeneralResponse deleteDiscount(UUID id) {
        Discount entity = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado con ID: " + id));
        discountRepository.delete(entity);

        return GeneralResponse.builder()
                .message("Descuento eliminado exitosamente.")
                .data(null)
                .build();
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
                .toList();
    }

    private void validateDates(DiscountRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessRuleException("La fecha de inicio no puede ser posterior a la que finalice");
        }
    }
}