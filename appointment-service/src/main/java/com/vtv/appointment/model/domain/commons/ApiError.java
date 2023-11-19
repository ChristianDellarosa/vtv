package com.vtv.appointment.model.domain.commons;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String description;
    private LocalDateTime timestamp;
    private String path;
    private ApiErrorDetail apiErrorDetail;
}