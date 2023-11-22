package com.vtv.auth.service.impl;

import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.exception.SessionException;
import com.vtv.auth.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_EXPIRED_CODE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_EXPIRED_DESCRIPTION;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_EXPIRED_MESSAGE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_DESCRIPTION;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_CODE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private SessionServiceImpl sessionService;


    @Test
    public void validate_Successfully() {
        final var bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4wMSIsImlzcyI6ImF1dGgtc2VydmljZSIsImV4cCI6MTcwMDUxMjU2MSwiaWF0IjoxNzAwNTAwNTYxLCJqdGkiOiIwZmRjMGI4MS03M2IwLTQ4MDYtODc3MC1kZGY0NDA0ODJiNDQifQ.yBJ3ZbGI91zz8KLn72Pau19uAwNdjfMecIrFZDyTXy8";

        doNothing().when(tokenService).validate(bearerToken);

        sessionService.validateSession(bearerToken);

        verify(tokenService).validate(bearerToken);
    }



    @Test
    public void validate_throw_tokenExpired() {
        final var bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4wMSIsImlzcyI6ImF1dGgtc2VydmljZSIsImV4cCI6MTcwMDUxMjU2MSwiaWF0IjoxNzAwNTAwNTYxLCJqdGkiOiIwZmRjMGI4MS03M2IwLTQ4MDYtODc3MC1kZGY0NDA0ODJiNDQifQ.yBJ3ZbGI91zz8KLn72Pau19uAwNdjfMecIrFZDyTXy8";
        final var exceptionExpected = new SessionException(
                ExceptionError.builder()
                        .description(TOKEN_EXPIRED_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(TOKEN_EXPIRED_CODE)
                                .message(TOKEN_EXPIRED_MESSAGE)
                                .build())
                        .build(), null);

        doThrow(exceptionExpected).when(tokenService).validate(bearerToken);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> sessionService.validateSession(bearerToken));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(tokenService).validate(bearerToken);
    }

    @Test
    public void validate_throw_tokenInvalid() {
        final var bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4wMSIsImlzcyI6ImF1dGgtc2VydmljZSIsImV4cCI6MTcwMDUxMjU2MSwiaWF0IjoxNzAwNTAwNTYxLCJqdGkiOiIwZmRjMGI4MS03M2IwLTQ4MDYtODc3MC1kZGY0NDA0ODJiNDQifQ.yBJ3ZbGI91zz8KLn72Pau19uAwNdjfMecIrFZDyTXy8";
        final var exceptionExpected = new SessionException(
                ExceptionError.builder()
                        .description(TOKEN_IS_INVALID_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(TOKEN_IS_INVALID_CODE)
                                .message(TOKEN_IS_INVALID_MESSAGE)
                                .build())
                        .build(), null);

        doThrow(exceptionExpected).when(tokenService).validate(bearerToken);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> sessionService.validateSession(bearerToken));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(tokenService).validate(bearerToken);
    }

}
