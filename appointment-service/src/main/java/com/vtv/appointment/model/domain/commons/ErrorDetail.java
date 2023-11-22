package com.vtv.appointment.model.domain.commons;

import lombok.Builder;

@Builder
public record ErrorDetail(Integer code, String message) {}