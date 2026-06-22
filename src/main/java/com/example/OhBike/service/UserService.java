package com.example.OhBike.service;

import com.example.OhBike.dto.request.UserUpdateRequest;
import com.example.OhBike.dto.response.UserResponse;

public interface UserService {
    UserResponse getMe(String email);
    UserResponse updateMe(String email, UserUpdateRequest request);
}
