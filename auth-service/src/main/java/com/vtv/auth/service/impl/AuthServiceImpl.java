package com.vtv.auth.service.impl;

import com.vtv.auth.exception.UserNotFoundException;
import com.vtv.auth.domain.SignIn;
import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.repository.UserRepository;
import com.vtv.auth.service.AuthService;
import com.vtv.auth.service.TokenService;
import com.vtv.auth.utils.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public static final Integer INVALID_CREDENTIALS_CODE = 200;
    public static final String  INVALID_CREDENTIALS_MESSAGE = "Credentials are invalid";
    public static final String  INVALID_CREDENTIALS_DESCRIPTION = "Credentials are invalid, please enter a valid credentials";

    public AuthServiceImpl(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public SignIn signIn(String basicAuthToken) {

        final var basicAuthentication = Base64Utils.getBasicAuthentication(basicAuthToken);

        final var username = basicAuthentication.username();

        userRepository.getByUsername(username)
                .map(user -> {
                    if(Objects.isNull(user.password())  || !user.password().equals(basicAuthentication.password())) {
                        log.info(INVALID_CREDENTIALS_DESCRIPTION);
                        throw new UserNotFoundException(
                                ExceptionError.builder()
                                        .description(INVALID_CREDENTIALS_DESCRIPTION)
                                        .errorDetail(ErrorDetail.builder()
                                                .code(INVALID_CREDENTIALS_CODE)
                                                .message(INVALID_CREDENTIALS_MESSAGE)
                                                .build())
                                        .build());
                    }
                    return user;
                }).orElseThrow(() -> {
                    log.info(INVALID_CREDENTIALS_DESCRIPTION);
                    return new UserNotFoundException(
                            ExceptionError.builder()
                                    .description(INVALID_CREDENTIALS_DESCRIPTION)
                                    .errorDetail(ErrorDetail.builder()
                                            .code(INVALID_CREDENTIALS_CODE)
                                            .message(INVALID_CREDENTIALS_MESSAGE)
                                            .build())
                                    .build());
                });

        final var accessToken = tokenService.generate(username);

        return SignIn.builder()
                .accessToken(accessToken)
                .build();
    }
}
