package com.example.OhBike.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private String uri;
    private Object message;
    private int status;
    private LocalDateTime time;
}