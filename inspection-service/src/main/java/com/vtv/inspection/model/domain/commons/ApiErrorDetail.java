package com.vtv.inspection.model.domain.commons;

import lombok.Builder;

@Builder
public record ApiErrorDetail(Integer code, String message) {}
