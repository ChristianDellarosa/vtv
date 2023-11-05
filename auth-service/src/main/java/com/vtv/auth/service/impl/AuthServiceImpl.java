package com.vtv.auth.service.impl;

import com.vtv.auth.model.SignIn;
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

    public AuthServiceImpl(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public SignIn signIn(String basicAuthToken) {

        final var basicAuthentication = Base64Utils.getBasicAuthentication(basicAuthToken);

        final var user = userRepository.getByUsername(basicAuthentication.username())
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND 404"));

        if(Objects.isNull(user.password())  || !user.password().equals(basicAuthentication.password())) {
            throw new RuntimeException("USER INVALID 401");
        }

        final var accessToken = tokenService.generate(basicAuthentication.username());

        return SignIn.builder()
                .accessToken(accessToken)
                .build();
    }
}
