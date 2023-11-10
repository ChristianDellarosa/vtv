package com.vtv.appointment.model.domain.commons;

import lombok.Builder;

@Builder
public record ExceptionError(String description, ErrorDetail errorDetail) {}