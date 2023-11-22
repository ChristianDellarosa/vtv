package com.vtv.auth.domain.commons;

import lombok.Builder;

@Builder
public record ApiErrorDetail(Integer code, String message) {}
