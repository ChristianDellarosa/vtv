package com.vtv.inspection.client;

import jakarta.validation.constraints.NotNull;

public interface AuthClient {
    void validateSession(@NotNull String token);
}
