package com.vtv.inspection.model.domain.commons;

import lombok.Builder;

@Builder
public record ExceptionError(String description, ErrorDetail errorDetail) {}