package com.vtv.inspection.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "check-test") //TODO: Is a testing config for simulate failed check
public class CheckConfiguration {
    private boolean isFailCheckCase;
    private boolean isLessThanFivePointsCheckCase;
}
