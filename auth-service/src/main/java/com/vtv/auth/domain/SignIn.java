package com.vtv.auth.domain;

import lombok.Builder;

@Builder
public record SignIn(String accessToken) { }
