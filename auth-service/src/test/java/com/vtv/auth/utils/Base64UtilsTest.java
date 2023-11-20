package com.vtv.auth.utils;

import com.vtv.auth.domain.BasicAuthentication;
import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.exception.SessionException;
import com.vtv.auth.exception.commons.GenericServerInternalException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_CODE;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_DESCRIPTION;
import static com.vtv.auth.service.impl.JwtTokenService.TOKEN_IS_INVALID_MESSAGE;
import static com.vtv.auth.utils.Base64Utils.DECODE_BASE64_ERROR_CODE;
import static com.vtv.auth.utils.Base64Utils.DECODE_BASE64_ERROR_DESCRIPTION;
import static com.vtv.auth.utils.Base64Utils.DECODE_BASE64_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class Base64UtilsTest {

    @Test
    public void getBasicAuthenticationWithValidToken() {
        final String baseAuthenticationToken = "Basic dGVzdDp0ZXN0MQ==";
        final var username = "test";
        final var password = "test1";
        final BasicAuthentication basicAuthentication = Base64Utils.getBasicAuthentication(baseAuthenticationToken);
        assertEquals(username, basicAuthentication.username());
        assertEquals(password, basicAuthentication.password());
    }

    @Test
    public void getBasicAuthenticationWithInvalidToken() {
        final String baseAuthenticationToken = "Basic ZGV2ZWxvcGVk";
        final var exceptionExpected = new GenericServerInternalException(
                ExceptionError.builder()
                        .description(DECODE_BASE64_ERROR_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .message(DECODE_BASE64_ERROR_MESSAGE)
                                .code(DECODE_BASE64_ERROR_CODE)
                                .build())
                        .build(), null);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> Base64Utils.getBasicAuthentication(baseAuthenticationToken));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());
    }
}