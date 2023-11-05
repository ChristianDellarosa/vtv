package com.vtv.inspection.client;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AuthClientImpl implements AuthClient {
    @Override
    public void validateSession(@NotNull String token) {

    }
}
