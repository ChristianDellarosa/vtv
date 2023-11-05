package com.vtv.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vtv.auth.configuration.JwtTokenConfiguration;
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
    public void validate(String token) {
        try {
            final var jwtToken = token.replace(TOKEN_TYPE, Strings.EMPTY).trim();
            DecodedJWT decodedJWT = verifier.verify(jwtToken);
        } catch (JWTVerificationException e) { //TODO: Ver excepcion y diferenciarlas
            throw new RuntimeException("USER NOT FOUND 404");
        }
    }
}
