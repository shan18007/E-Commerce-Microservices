package com.ecommerce.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("User Service API Doc")
                        .description("User service manages all the User operations of E-Commerce")
                        .version("1.0"));
    }

}