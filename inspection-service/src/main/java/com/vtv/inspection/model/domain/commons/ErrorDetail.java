package com.vtv.inspection.model.domain.commons;

import lombok.Builder;

@Builder
public record ErrorDetail(Integer code, String message) {}