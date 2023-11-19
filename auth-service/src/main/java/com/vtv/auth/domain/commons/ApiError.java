package com.vtv.auth.domain.commons;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiError(String description, LocalDateTime timestamp, String path, ApiErrorDetail apiErrorDetail) {}
