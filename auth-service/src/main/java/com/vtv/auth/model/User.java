package com.vtv.auth.model;

import lombok.Builder;


@Builder
public record User(String username, String password, String name) { }