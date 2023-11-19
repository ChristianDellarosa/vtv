package com.vtv.auth.utils;

import com.vtv.auth.exception.commons.GenericServerInternalException;
import com.vtv.auth.domain.BasicAuthentication;
import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class Base64Utils {

    private static final String AUTHENTICATION_TYPE = "Basic";
    private static final int FIRST_INDEX = 0;

    private static final String DELIMITER = ":";
    private static final int SECOND_INDEX = 1;

    private static final String DECODE_BASE64_ERROR_DESCRIPTION = "An internal server error occurs";
    private static final Integer DECODE_BASE64_ERROR_CODE = 10; //TODO: Generic Code of internal error
    private static final String DECODE_BASE64_ERROR_MESSAGE = "An error occurred while decoding the basic auth token";

    public static BasicAuthentication getBasicAuthentication(String baseAuthenticationToken) {
        try {
            final var base64 = baseAuthenticationToken.replace(AUTHENTICATION_TYPE, Strings.EMPTY).trim();
            final var tokenDecoded = new String(Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8)));

            final String username = tokenDecoded.split(DELIMITER)[FIRST_INDEX];

            final String password = tokenDecoded.split(DELIMITER)[SECOND_INDEX];

            return BasicAuthentication.builder()
                    .username(username)
                    .password(password)
                    .build();

        } catch (Exception exception) {
            log.error(DECODE_BASE64_ERROR_MESSAGE);
            throw new GenericServerInternalException(
                    ExceptionError.builder()
                            .description(DECODE_BASE64_ERROR_DESCRIPTION)
                            .errorDetail(ErrorDetail.builder()
                                    .message(DECODE_BASE64_ERROR_MESSAGE)
                                    .code(DECODE_BASE64_ERROR_CODE)
                                    .build())
                    .build(), exception);
        }





    }
    //TODO: Manejo de error?
}
