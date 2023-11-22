package com.vtv.inspection.client;

import com.vtv.inspection.configuration.AuthClientConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthClientImplTest {
    @Mock
    private AuthClientConfiguration authClientConfiguration;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthClientImpl authClient;

    private static final String BASE_URL = "https://aBaseUrl.com";

    private static final String VALIDATE_SESSION_RESOURCE = "validateSessionResource";

    @BeforeEach
    public void setUp() {
        when(authClientConfiguration.getBaseUrl())
                .thenReturn(BASE_URL);

        when(authClientConfiguration.getResources())
                .thenReturn(AuthClientConfiguration.Resources
                        .builder()
                        .validateSession(VALIDATE_SESSION_RESOURCE)
                        .build());
    }

    @Test
    public void validateSessionSuccessfully() {
        final var token = "token";

        authClient.validateSession(token);

        verify(authClientConfiguration).getResources();
        verify(restTemplate).postForObject(any(), any(), any());
    }
}