package com.vtv.auth.model;

import lombok.Builder;

@Builder
public record BasicAuthentication(String username, String password) { }
