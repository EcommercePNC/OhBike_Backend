package com.example.OhBike.service;

import com.example.OhBike.dto.request.OrderRequest;
import com.example.OhBike.dto.response.GeneralResponse;
import java.util.UUID;

public interface OrderService {

    GeneralResponse create(OrderRequest request);

    GeneralResponse getById(UUID id);
}
