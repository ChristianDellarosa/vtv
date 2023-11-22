package com.vtv.auth.domain.commons;

import lombok.Builder;

@Builder
public record ExceptionError(String description, ErrorDetail errorDetail) {}