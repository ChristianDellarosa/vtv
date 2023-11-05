package com.vtv.auth.model.commons;

import lombok.Builder;

@Builder
public record ExceptionError(String description, ErrorDetail errorDetail) {}