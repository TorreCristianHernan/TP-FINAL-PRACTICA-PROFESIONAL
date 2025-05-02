package com.example.dynamicapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}
