package com.vtv.inspection.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI metadata() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inspection API")
                        .description("Inspection API for VTV Project")
                        .version("1.0.0"));
    }

}