package com.example.dynamicapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Dynamic API")
                .version("0.0.1")
                .description("API para consultas dinámicas con Spring Boot")
                .contact(new Contact().name("Rodrigo").email("rodrigo@example.com"))
                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
            );
    }
}
