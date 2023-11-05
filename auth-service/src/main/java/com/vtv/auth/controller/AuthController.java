package com.vtv.auth.controller;

import com.vtv.auth.model.SignIn;
import com.vtv.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public SignIn signIn(@RequestHeader(name= HttpHeaders.AUTHORIZATION) String basicAuthorizationToken) {
        return authService.signIn(basicAuthorizationToken);
    }
}