package com.vtv.auth.domain.commons;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
public record ApiError(String description, LocalDateTime timestamp, String path, ApiErrorDetail apiErrorDetail) {}
