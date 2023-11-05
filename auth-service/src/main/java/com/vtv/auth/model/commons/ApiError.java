package com.vtv.auth.model.commons;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ApiError {
    private String description;
    private LocalDateTime timestamp;
    private String path;
    private ApiErrorDetail apiErrorDetail;
}