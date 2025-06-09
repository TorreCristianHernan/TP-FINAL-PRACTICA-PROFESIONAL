package com.example.dynamicapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API REST Dinámica",
                version = "1.0",
                description = "API para consultar tablas de forma dinámica en una base de datos MySQL"
        )
)
public class DynamicApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicApiApplication.class, args);
    }

    // 👇 Configuración de CORS para permitir el acceso desde el frontend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Aplica a todos los endpoints
                        .allowedOrigins("http://localhost:4200") // URL del frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*"); // Permite todos los headers
            }
        };
    }
}
