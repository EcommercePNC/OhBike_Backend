package com.example.OhBike.controller;

import com.example.OhBike.dto.request.RoleUpdateRequest;
import com.example.OhBike.dto.response.UserResponse;
import com.example.OhBike.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleUpdateRequest request) {

        UserResponse response = adminUserService.updateUserRole(id, request);
        return ResponseEntity.ok(response);
    }
}
