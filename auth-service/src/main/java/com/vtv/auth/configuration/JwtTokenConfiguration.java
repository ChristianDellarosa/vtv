package com.vtv.auth.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "token.basic-auth")
public class JwtTokenConfiguration {

    private Integer expireAtInSeconds;
    private String secret;
}
