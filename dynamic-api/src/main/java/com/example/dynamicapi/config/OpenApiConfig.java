package com.example.dynamicapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8093" + contextPath)
                                .description("Servidor de desarrollo local")
                ))
                .info(new Info()
                        .title("API REST Dinámica para MySQL")
                        .description("API para consultar tablas de forma dinámica en una base de datos MySQL alojada en Aiven")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Ferrefull")
                                .email("contacto@ferrefull.com")
                                .url("https://ferrefull.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
