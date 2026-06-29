package com.example.OhBike.service.impl;

import com.example.OhBike.dto.response.OrderDetailResponse;
import com.example.OhBike.mapper.OrderMapper;
import com.example.OhBike.repository.OrderDetailRepository;
import com.example.OhBike.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDetailResponse> getVendorOrderItems() {
        return orderDetailRepository.findAll()
                .stream()
                .map(orderMapper::toDetailDto)
                .toList();
    }

    @Override
    public List<OrderDetailResponse> getAdminOrderItems() {
        return orderDetailRepository.findAll()
                .stream()
                .map(orderMapper::toDetailDto)
                .toList();
    }
}