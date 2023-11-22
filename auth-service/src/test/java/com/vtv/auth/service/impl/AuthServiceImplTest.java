package com.vtv.auth.service.impl;

import com.vtv.auth.domain.SignIn;
import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.exception.UserNotFoundException;
import com.vtv.auth.mock.UserFactory;
import com.vtv.auth.repository.UserRepository;
import com.vtv.auth.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.vtv.auth.service.impl.AuthServiceImpl.INVALID_CREDENTIALS_CODE;
import static com.vtv.auth.service.impl.AuthServiceImpl.INVALID_CREDENTIALS_DESCRIPTION;
import static com.vtv.auth.service.impl.AuthServiceImpl.INVALID_CREDENTIALS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    public void signIn_return_TokenSuccessfully() {
        final var base64BasicAuth = "Q2hyaXN0aWFuOnBhc3N3b3Jk";
        final var bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDaHJpc3RpYW4wMSIsImlzcyI6ImF1dGgtc2VydmljZSIsImV4cCI6MTcwMDUxMjU2MSwiaWF0IjoxNzAwNTAwNTYxLCJqdGkiOiIwZmRjMGI4MS03M2IwLTQ4MDYtODc3MC1kZGY0NDA0ODJiNDQifQ.yBJ3ZbGI91zz8KLn72Pau19uAwNdjfMecIrFZDyTXy8";
        final var userMock = UserFactory.buildValidUser();
        final var responseExpected = SignIn.builder()
                .accessToken(bearerToken)
                .build();

        when(userRepository.getByUsername(userMock.username()))
                .thenReturn(Optional.of(userMock));

        when(tokenService.generate(userMock.username()))
                .thenReturn(bearerToken);

        final var response = authService.signIn(base64BasicAuth);

        assertEquals(response, responseExpected);

        verify(userRepository).getByUsername(userMock.username());
        verify(tokenService).generate(userMock.username());
    }


    @Test
    public void signIn_when_usernameNotExists_throw_UserNotFoundException() {
        final var username = "Christian";
        final var base64BasicAuth = "Q2hyaXN0aWFuOnBhc3N3b3Jk";

        final var exceptionExpected = new UserNotFoundException(ExceptionError.builder()
                .description(INVALID_CREDENTIALS_DESCRIPTION)
                .errorDetail(ErrorDetail.builder()
                        .code(INVALID_CREDENTIALS_CODE)
                        .message(INVALID_CREDENTIALS_MESSAGE)
                        .build())
                .build());

        when(userRepository.getByUsername(username))
                .thenReturn(Optional.empty());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> authService.signIn(base64BasicAuth));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(userRepository).getByUsername(username);
        verify(tokenService, never()).generate(anyString());
    }

    @Test
    public void signIn_when_passwordNotMatch_throw_UserNotFoundException() {
        final var base64BasicAuth = "Q2hyaXN0aWFuOnBhc3N3b3Jk";
        final var userMock = UserFactory.buildInvalidUser();

        final var exceptionExpected = new UserNotFoundException(ExceptionError.builder()
                .description(INVALID_CREDENTIALS_DESCRIPTION)
                .errorDetail(ErrorDetail.builder()
                        .code(INVALID_CREDENTIALS_CODE)
                        .message(INVALID_CREDENTIALS_MESSAGE)
                        .build())
                .build());

        when(userRepository.getByUsername(userMock.username()))
                .thenReturn(Optional.of(userMock));

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> authService.signIn(base64BasicAuth));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(userRepository).getByUsername(userMock.username());
        verify(tokenService, never()).generate(anyString());
    }
}
