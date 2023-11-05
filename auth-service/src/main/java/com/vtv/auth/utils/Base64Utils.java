package com.vtv.auth.utils;

import com.vtv.auth.model.BasicAuthentication;
import org.apache.logging.log4j.util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    private static final String AUTHENTICATION_TYPE = "Basic";
    private static final int FIRST_INDEX = 0;

    private static final String DELIMITER = ":";
    private static final int SECOND_INDEX = 1;

    public static BasicAuthentication getBasicAuthentication(String baseAuthenticationToken) {
        final var base64 = baseAuthenticationToken.replace(AUTHENTICATION_TYPE, Strings.EMPTY).trim();
        final var tokenDecoded = new String(Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8)));

        final var username = tokenDecoded.split(DELIMITER)[FIRST_INDEX];

        final var password = tokenDecoded.split(DELIMITER)[SECOND_INDEX];

        return BasicAuthentication.builder()
                .username(username)
                .password(password)
                .build();
    }
    //TODO: Manejo de error?
}
