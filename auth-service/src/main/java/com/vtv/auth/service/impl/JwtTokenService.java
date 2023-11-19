package com.vtv.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.vtv.auth.configuration.JwtTokenConfiguration;
import com.vtv.auth.exception.SessionException;
import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.service.TokenService;
import com.vtv.auth.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class JwtTokenService implements TokenService {
    private final JwtTokenConfiguration jwtTokenConfiguration;
    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    private final static String TOKEN_TYPE = "Bearer";
    private final static String ISSUER = "auth-service";

    private static final String TOKEN_EXPIRED_DESCRIPTION = "Token is expired, please log in again to get a new token.";
    private static final Integer TOKEN_EXPIRED_CODE = 100;
    private static final String  TOKEN_EXPIRED_MESSAGE = "Token is expired";

    private static final String TOKEN_IS_INVALID_DESCRIPTION = "Token is invalid, please enter a valid token";
    private static final Integer TOKEN_IS_INVALID_DESCRIPTION_CODE = 101;
    private static final String  TOKEN_IS_INVALID_DESCRIPTION_MESSAGE = "Token is invalid";


    public JwtTokenService(JwtTokenConfiguration jwtTokenConfiguration) {
        this.jwtTokenConfiguration = jwtTokenConfiguration;
        this.algorithm = Algorithm.HMAC256(jwtTokenConfiguration.getSecret());
        this.verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
    }

    @Override
    public String generate(String username) {
        log.info("Generate access token by user {}", username);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withIssuedAt(LocalDateTime.now()
                        .atZone(DateUtils.getZoneId())
                        .toInstant())
                .withExpiresAt(
                        LocalDateTime.now()
                        .atZone(DateUtils.getZoneId())
                        .plusSeconds(jwtTokenConfiguration.getExpireAtInSeconds())
                        .toInstant())
                .withJWTId(UUID.randomUUID()
                        .toString())
                .sign(algorithm);
    }

    @Override
    public void validate(String token) { //TODO: Quizas faltaría verificar que ese token es de quien dice ser, sino cualquiera que tenga este token podria acceder
        log.info("Validate access token..");
        try {
            final var jwtToken = token.replace(TOKEN_TYPE, Strings.EMPTY).trim();
            verifier.verify(jwtToken);
        } catch (TokenExpiredException tokenExpiredException) {
            log.info(TOKEN_EXPIRED_MESSAGE, tokenExpiredException);
            throw new SessionException(
                    ExceptionError.builder()
                            .description(TOKEN_EXPIRED_DESCRIPTION)
                            .errorDetail(ErrorDetail.builder()
                                    .code(TOKEN_EXPIRED_CODE)
                                    .message(TOKEN_EXPIRED_MESSAGE)
                                    .build())
                    .build(),
                    tokenExpiredException);
        } catch (SignatureVerificationException | AlgorithmMismatchException | InvalidClaimException invalidTokenException) {
            log.info(TOKEN_IS_INVALID_DESCRIPTION, invalidTokenException);
            throw new SessionException(
                    ExceptionError.builder()
                            .description(TOKEN_IS_INVALID_DESCRIPTION)
                            .errorDetail(ErrorDetail.builder()
                                    .code(TOKEN_IS_INVALID_DESCRIPTION_CODE)
                                    .message(TOKEN_IS_INVALID_DESCRIPTION_MESSAGE)
                                    .build())
                            .build(),
                    invalidTokenException.getCause());
        }
    }
}
//TODO: BUG CON LONGITUD MAS CHICA DE TOKEN