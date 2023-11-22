package com.vtv.auth.service.impl;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.vtv.auth.configuration.JwtTokenConfiguration;
import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.exception.SessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_EXPIRED_CODE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_EXPIRED_DESCRIPTION;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_EXPIRED_MESSAGE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_DESCRIPTION;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_CODE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    private final static String SECRET = "hola";

    private final static Integer EXPIRED_AT_SECONDS = 200;
    @Mock
    private JwtTokenConfiguration jwtTokenConfiguration;

    private JwtTokenService jwtTokenService;

    @BeforeEach
    public void setUp() {
        when(jwtTokenConfiguration.getSecret()).thenReturn(SECRET);
        jwtTokenService = new JwtTokenService(jwtTokenConfiguration);
    }

    @Test
    public void generate_Return_TokenSuccessfully() {
        when(jwtTokenConfiguration.getExpireAtInSeconds()).thenReturn(EXPIRED_AT_SECONDS);

        final var username = "Christian";
        final String token = jwtTokenService.generate(username);

        assertNotNull(token);
    }

    @Test
    public void validate_WithValidToken_Return_Successfully() {

        final String username = "Christian";
        final String token = jwtTokenService.generate(username);

        jwtTokenService.validate(token);
    }

    @Test
    public void validate_WithExpiredToken_Throw_SessionException_TokenExpired() {

        final var exceptionExpected = new SessionException(
                ExceptionError.builder()
                        .description(TOKEN_EXPIRED_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(TOKEN_EXPIRED_CODE)
                                .message(TOKEN_EXPIRED_MESSAGE)
                                .build())
                        .build(), null);

        final String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4iLCJpc3MiOiJhdXRoLXNlcnZpY2UiLCJleHAiOjE3MDA1MDU5ODIsImlhdCI6MTcwMDUwNTk4MSwianRpIjoiNTdmNTFhN2EtZmU4Mi00MGUyLTlhMmUtMWRhZWIxNGY1YjVhIn0.2dyDYGUHq4hG0uprwgK0cNpHXJwS7XkL6MhsGAxYULk";

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> jwtTokenService.validate(expiredToken));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());
    }

    @Test
    public void validate_WithExpiredToken_Throw_SessionException_TokenInvalid() {

        final var exceptionExpected = new SessionException(
                ExceptionError.builder()
                        .description(TOKEN_IS_INVALID_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(TOKEN_IS_INVALID_CODE)
                                .message(TOKEN_IS_INVALID_MESSAGE)
                                .build())
                        .build(), null);

        final String invalidToken = "BadToken";

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> jwtTokenService.validate(invalidToken));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());
    }
}
