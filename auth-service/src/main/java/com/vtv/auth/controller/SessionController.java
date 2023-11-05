package com.vtv.auth.controller;

import com.vtv.auth.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/session")
@Slf4j
public class SessionController {

    private final SessionService sessionService;

    public SessionController( SessionService sessionService) {
        this.sessionService = sessionService;

    }

    @PostMapping
    public void validateSessionToken(@RequestHeader(name="Authorization") String token) {
        sessionService.validateSession(token);
    }
}