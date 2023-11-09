package com.vtv.inspection.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.inspection.client.AuthClientInterceptor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "client.auth")
public class AuthClientConfiguration {
    public static final String CLIENT_AUTH_REST_TEMPLATE = "CLIENT_AUTH_REST_TEMPLATE";

    private String baseUrl;

    private AuthClientConfiguration.Resources resources;
    private Integer restClientTimeoutInMs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resources {
        private String validateSession;
    }

    @Bean(name = CLIENT_AUTH_REST_TEMPLATE)
    public RestTemplate restTemplate(RestTemplateBuilder builder, AuthClientInterceptor authClientInterceptor) {
        return builder.setReadTimeout(Duration.ofMillis(restClientTimeoutInMs))
                .interceptors(authClientInterceptor)
                .build();
    }

    @Bean
    public AuthClientInterceptor getClientsAuthSignUpInterceptor(ObjectMapper mapper) {
        return new AuthClientInterceptor(mapper);
    }

}
