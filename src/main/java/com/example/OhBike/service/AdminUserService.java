package com.example.OhBike.service;

import com.example.OhBike.dto.request.RoleUpdateRequest;
import com.example.OhBike.dto.response.UserResponse;

import java.util.UUID;

public interface AdminUserService {
    UserResponse updateUserRole(UUID userId, RoleUpdateRequest request);
}
