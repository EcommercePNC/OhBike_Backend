package com.example.OhBike.services;

import com.example.OhBike.dto.request.DiscountRequest;
import com.example.OhBike.dto.response.DiscountResponse;
import com.example.OhBike.dtos.GeneralResponse;
import com.example.OhBike.entities.Discount;
import com.example.OhBike.repositories.DiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;

    public GeneralResponse create(DiscountRequest request) {
        validateDates(request);
        Discount entity = Discount.builder()
                .name(request.getName())
                .discountType(request.getDiscountType())
                .value(request.getValue())
                .active(request.getActive())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        Discount saved = discountRepository.save(entity);
        return new GeneralResponse("Descuento creado exitosamente.", convertToResponse(saved));
    }

    public GeneralResponse update(UUID id, DiscountRequest request) {
        validateDates(request);
        Discount entity = discountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Descuento no encontrado con ID: " + id));
        entity.setName(request.getName());
        entity.setDiscountType(request.getDiscountType());
        entity.setValue(request.getValue());
        entity.setActive(request.getActive());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        Discount updated = discountRepository.save(entity);
        return new GeneralResponse("Descuento actualizado exitosamente.", convertToResponse(updated));
    }

    public GeneralResponse delete(UUID id) {
        Discount entity = discountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Descuento no encontrado con ID: " + id));
        discountRepository.delete(entity);
        return new GeneralResponse("Descuento eliminado exitosamente.", null);
    }

    @Transactional(readOnly = true)
    public DiscountResponse getById(UUID id) {
        return discountRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new NoSuchElementException("Descuento no encontrado con ID: " + id));
    }

    //listar descuentos activos
    public List<DiscountResponse> findAllActive() {
        return discountRepository.findByActiveTrue().stream()
                .map(this::convertToResponse)
                .toList();
    }

    //validacion de fechas
    private void validateDates(DiscountRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la que finalice");
        }
    }

    private DiscountResponse convertToResponse(Discount entity) {
        return DiscountResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .discountType(entity.getDiscountType())
                .value(entity.getValue())
                .active(entity.getActive())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
