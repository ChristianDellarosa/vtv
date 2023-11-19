package com.vtv.inspection.client;

import com.vtv.inspection.configuration.AuthClientConfiguration;
import com.vtv.inspection.exception.commons.GenericServerInternalException;
import com.vtv.inspection.exception.commons.GenericUnauthorizedException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.vtv.inspection.configuration.AuthClientConfiguration.*;

@Service
@Slf4j
public class AuthClientImpl implements AuthClient {

    private final AuthClientConfiguration authClientConfiguration;
    private final RestTemplate restTemplate;

    public AuthClientImpl(AuthClientConfiguration authClientConfiguration,
                          @Qualifier(CLIENT_AUTH_REST_TEMPLATE) RestTemplate restTemplate) {
        this.authClientConfiguration = authClientConfiguration;
        this.restTemplate = restTemplate;
    }

    @Override
    public void validateSession(@NotNull String token) throws GenericServerInternalException, GenericUnauthorizedException {
        final var uri = UriComponentsBuilder
                .fromHttpUrl(authClientConfiguration.getBaseUrl())
                .path(authClientConfiguration.getResources().getValidateSession())
                .build()
                .toUri();

        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        final HttpEntity<String> entity = new HttpEntity<>(headers);

            restTemplate.postForObject(
                    uri,
                    entity, Void.class);
    }
}
