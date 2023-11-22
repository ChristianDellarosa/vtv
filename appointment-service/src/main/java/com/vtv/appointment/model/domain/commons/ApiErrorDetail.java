package com.vtv.appointment.model.domain.commons;

import lombok.Builder;

@Builder
public record ApiErrorDetail(Integer code, String message) {}
