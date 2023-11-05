package com.vtv.auth.model.commons;

import lombok.Builder;

@Builder
public record ApiErrorDetail(Integer code, String message) {}
