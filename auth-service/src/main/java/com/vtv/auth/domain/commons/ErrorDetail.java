package com.vtv.auth.domain.commons;

import lombok.Builder;

@Builder
public record ErrorDetail(Integer code, String message) {}