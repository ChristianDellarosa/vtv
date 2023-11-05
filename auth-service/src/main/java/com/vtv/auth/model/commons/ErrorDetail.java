package com.vtv.auth.model.commons;

import lombok.Builder;

@Builder
public record ErrorDetail(Integer code, String message) {}