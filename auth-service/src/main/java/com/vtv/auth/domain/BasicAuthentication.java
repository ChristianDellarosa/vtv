package com.vtv.auth.domain;

import lombok.Builder;

@Builder
public record BasicAuthentication(String username, String password) { }
