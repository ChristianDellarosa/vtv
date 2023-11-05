package com.vtv.auth.model;

import lombok.*;

@Builder
public record SignInRequest(String username, String password) { }
