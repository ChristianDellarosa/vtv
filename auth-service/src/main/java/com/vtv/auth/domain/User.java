package com.vtv.auth.domain;

import lombok.Builder;


@Builder
public record User(String username, String password, String name) { }