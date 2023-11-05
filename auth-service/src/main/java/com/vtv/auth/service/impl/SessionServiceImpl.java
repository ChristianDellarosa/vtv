package com.vtv.auth.service.impl;

import com.vtv.auth.service.SessionService;
import com.vtv.auth.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {
    private final TokenService tokenService;

    public SessionServiceImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void validateSession(String token) {
        tokenService.validate(token); //TODO: Falta manejo de errores
    }
}
